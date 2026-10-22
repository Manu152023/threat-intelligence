import { useCallback, useEffect, useState } from 'react'
import { createThreat, deleteThreat, getStats, listThreats, updateThreat } from './api'

const SEVERITIES = ['CRITICAL', 'HIGH', 'MEDIUM', 'LOW']
const CATEGORIES = ['MALWARE', 'PHISHING', 'RANSOMWARE', 'DDOS', 'VULNERABILITY', 'DATA_BREACH', 'OTHER']
const STATUSES = ['ACTIVE', 'INVESTIGATING', 'MITIGATED', 'RESOLVED']
const EMPTY_FORM = { title: '', description: '', severity: 'MEDIUM', category: 'MALWARE', status: 'ACTIVE', source: '' }
const label = (value) => value.replace('_', ' ')

function Badge({ value, type }) {
  return <span className={`badge ${type}-${value}`}>{label(value)}</span>
}

function ThreatForm({ initial, onSave, onCancel }) {
  const [form, setForm] = useState(initial)
  const [error, setError] = useState('')
  const [saving, setSaving] = useState(false)
  const set = (field) => (e) => setForm({ ...form, [field]: e.target.value })

  async function submit(e) {
    e.preventDefault()
    setSaving(true)
    setError('')
    try {
      await onSave(form)
    } catch (err) {
      setError(err.message)
      setSaving(false)
    }
  }

  return (
    <div className="overlay" onClick={onCancel}>
      <form className="modal" onClick={(e) => e.stopPropagation()} onSubmit={submit}>
        <h2>{form.id ? 'Edit threat' : 'Add threat'}</h2>
        <label>Title<input value={form.title} onChange={set('title')} maxLength={255} required /></label>
        <label>Description<textarea value={form.description} onChange={set('description')} rows={3} maxLength={4000} required /></label>
        <div className="row">
          <label>Severity
            <select value={form.severity} onChange={set('severity')}>{SEVERITIES.map((v) => <option key={v}>{v}</option>)}</select>
          </label>
          <label>Category
            <select value={form.category} onChange={set('category')}>{CATEGORIES.map((v) => <option key={v} value={v}>{label(v)}</option>)}</select>
          </label>
          <label>Status
            <select value={form.status} onChange={set('status')}>{STATUSES.map((v) => <option key={v}>{v}</option>)}</select>
          </label>
        </div>
        <label>Source<input value={form.source || ''} onChange={set('source')} maxLength={100} /></label>
        {error && <p className="error">{error}</p>}
        <div className="actions">
          <button type="button" className="ghost" onClick={onCancel}>Cancel</button>
          <button type="submit" disabled={saving}>{saving ? 'Saving...' : 'Save'}</button>
        </div>
      </form>
    </div>
  )
}

export default function App() {
  const [threats, setThreats] = useState([])
  const [stats, setStats] = useState(null)
  const [filters, setFilters] = useState({ q: '', severity: '', category: '', status: '' })
  const [editing, setEditing] = useState(null)
  const [error, setError] = useState('')
  const [loading, setLoading] = useState(true)

  const load = useCallback(async () => {
    try {
      setLoading(true)
      const [list, summary] = await Promise.all([listThreats(filters), getStats()])
      setThreats(list)
      setStats(summary)
      setError('')
    } catch (err) {
      setError(err.message)
    } finally {
      setLoading(false)
    }
  }, [filters])

  // Small delay so typing in the search box does not fire a request on every keystroke.
  useEffect(() => {
    const timer = setTimeout(load, 250)
    return () => clearTimeout(timer)
  }, [load])

  const setFilter = (field) => (e) => setFilters({ ...filters, [field]: e.target.value })

  async function save(form) {
    const { id, createdAt, updatedAt, ...data } = form
    if (id) await updateThreat(id, data)
    else await createThreat(data)
    setEditing(null)
    load()
  }

  async function remove(threat) {
    if (!window.confirm(`Delete "${threat.title}"?`)) return
    try {
      await deleteThreat(threat.id)
      load()
    } catch (err) {
      setError(err.message)
    }
  }

  return (
    <div className="container">
      <header>
        <div>
          <h1>Threat Intelligence Platform</h1>
          <p className="muted">Track, filter and manage security threats</p>
        </div>
        <button onClick={() => setEditing(EMPTY_FORM)}>+ Add threat</button>
      </header>

      {stats && (
        <section className="stats">
          <div className="card"><span className="muted">Total</span><strong>{stats.total}</strong></div>
          {SEVERITIES.map((s) => (
            <div className={`card sev-${s}`} key={s}><span className="muted">{s}</span><strong>{stats.bySeverity[s]}</strong></div>
          ))}
          <div className="card"><span className="muted">Active</span><strong>{stats.byStatus.ACTIVE}</strong></div>
        </section>
      )}

      <section className="filters">
        <input placeholder="Search title, description, source..." value={filters.q} onChange={setFilter('q')} />
        <select value={filters.severity} onChange={setFilter('severity')}>
          <option value="">All severities</option>{SEVERITIES.map((v) => <option key={v}>{v}</option>)}
        </select>
        <select value={filters.category} onChange={setFilter('category')}>
          <option value="">All categories</option>{CATEGORIES.map((v) => <option key={v} value={v}>{label(v)}</option>)}
        </select>
        <select value={filters.status} onChange={setFilter('status')}>
          <option value="">All statuses</option>{STATUSES.map((v) => <option key={v}>{v}</option>)}
        </select>
      </section>

      {error && <p className="error">{error}</p>}

      <div className="table-wrap">
        <table>
          <thead>
            <tr><th>Title</th><th>Severity</th><th>Category</th><th>Status</th><th>Source</th><th></th></tr>
          </thead>
          <tbody>
            {threats.map((t) => (
              <tr key={t.id}>
                <td><strong>{t.title}</strong><div className="muted small">{t.description}</div></td>
                <td><Badge value={t.severity} type="sev" /></td>
                <td>{label(t.category)}</td>
                <td><Badge value={t.status} type="st" /></td>
                <td>{t.source || '-'}</td>
                <td className="row-actions">
                  <button className="ghost" onClick={() => setEditing(t)}>Edit</button>
                  <button className="danger" onClick={() => remove(t)}>Delete</button>
                </td>
              </tr>
            ))}
            {!loading && threats.length === 0 && (
              <tr><td colSpan={6} className="muted center">No threats match your filters.</td></tr>
            )}
          </tbody>
        </table>
      </div>

      {editing && <ThreatForm initial={editing} onSave={save} onCancel={() => setEditing(null)} />}
    </div>
  )
}
