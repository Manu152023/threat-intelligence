package com.threatintel.service;

import com.threatintel.dto.ThreatRequest;
import com.threatintel.exception.ResourceNotFoundException;
import com.threatintel.model.Category;
import com.threatintel.model.Severity;
import com.threatintel.model.Status;
import com.threatintel.model.Threat;
import com.threatintel.repository.ThreatRepository;
import jakarta.persistence.criteria.Predicate;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ThreatService {

    private final ThreatRepository repository;

    public ThreatService(ThreatRepository repository) {
        this.repository = repository;
    }

    @Transactional(readOnly = true)
    public List<Threat> search(String q, Severity severity, Category category, Status status) {
        Specification<Threat> spec = (root, query, cb) -> {
            List<Predicate> predicates = new ArrayList<>();
            if (q != null && !q.isBlank()) {
                String like = "%" + q.trim().toLowerCase() + "%";
                predicates.add(cb.or(
                        cb.like(cb.lower(root.get("title")), like),
                        cb.like(cb.lower(root.get("description")), like),
                        cb.like(cb.lower(root.get("source")), like)));
            }
            if (severity != null) predicates.add(cb.equal(root.get("severity"), severity));
            if (category != null) predicates.add(cb.equal(root.get("category"), category));
            if (status != null) predicates.add(cb.equal(root.get("status"), status));
            return cb.and(predicates.toArray(new Predicate[0]));
        };
        return repository.findAll(spec, Sort.by(Sort.Direction.DESC, "createdAt"));
    }

    @Transactional(readOnly = true)
    public Threat get(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Threat " + id + " not found"));
    }

    @Transactional
    public Threat create(ThreatRequest request) {
        Threat threat = new Threat();
        apply(threat, request);
        return repository.save(threat);
    }

    @Transactional
    public Threat update(Long id, ThreatRequest request) {
        Threat threat = get(id);
        apply(threat, request);
        return repository.save(threat);
    }

    @Transactional
    public void delete(Long id) {
        repository.delete(get(id));
    }

    @Transactional(readOnly = true)
    public Map<String, Object> stats() {
        Map<String, Long> bySeverity = new LinkedHashMap<>();
        for (Severity s : Severity.values()) bySeverity.put(s.name(), repository.countBySeverity(s));
        Map<String, Long> byStatus = new LinkedHashMap<>();
        for (Status s : Status.values()) byStatus.put(s.name(), repository.countByStatus(s));
        Map<String, Object> result = new LinkedHashMap<>();
        result.put("total", repository.count());
        result.put("bySeverity", bySeverity);
        result.put("byStatus", byStatus);
        return result;
    }

    private void apply(Threat threat, ThreatRequest r) {
        threat.setTitle(r.title().trim());
        threat.setDescription(r.description().trim());
        threat.setSeverity(r.severity());
        threat.setCategory(r.category());
        threat.setStatus(r.status());
        threat.setSource(r.source() == null || r.source().isBlank() ? null : r.source().trim());
    }
}
