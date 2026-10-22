const BASE = import.meta.env.VITE_API_URL || ''

async function request(path, options = {}) {
  const res = await fetch(BASE + path, {
    headers: { 'Content-Type': 'application/json' },
    ...options,
  })
  if (!res.ok) {
    let message = `Request failed (${res.status})`
    try {
      const data = await res.json()
      if (data.errors) message = Object.values(data.errors).join(', ')
      else if (data.message) message = data.message
    } catch {
      /* response had no JSON body */
    }
    throw new Error(message)
  }
  return res.status === 204 ? null : res.json()
}

export function listThreats(filters) {
  const params = new URLSearchParams()
  Object.entries(filters).forEach(([key, value]) => {
    if (value) params.set(key, value)
  })
  return request(`/api/threats?${params}`)
}

export const getStats = () => request('/api/threats/stats')
export const createThreat = (data) =>
  request('/api/threats', { method: 'POST', body: JSON.stringify(data) })
export const updateThreat = (id, data) =>
  request(`/api/threats/${id}`, { method: 'PUT', body: JSON.stringify(data) })
export const deleteThreat = (id) => request(`/api/threats/${id}`, { method: 'DELETE' })
