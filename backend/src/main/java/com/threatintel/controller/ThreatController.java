package com.threatintel.controller;

import com.threatintel.dto.ThreatRequest;
import com.threatintel.model.Category;
import com.threatintel.model.Severity;
import com.threatintel.model.Status;
import com.threatintel.model.Threat;
import com.threatintel.service.ThreatService;
import jakarta.validation.Valid;
import java.util.List;
import java.util.Map;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/threats")
public class ThreatController {

    private final ThreatService service;

    public ThreatController(ThreatService service) {
        this.service = service;
    }

    @GetMapping
    public List<Threat> list(@RequestParam(required = false) String q,
                             @RequestParam(required = false) Severity severity,
                             @RequestParam(required = false) Category category,
                             @RequestParam(required = false) Status status) {
        return service.search(q, severity, category, status);
    }

    @GetMapping("/stats")
    public Map<String, Object> stats() {
        return service.stats();
    }

    @GetMapping("/{id}")
    public Threat get(@PathVariable Long id) {
        return service.get(id);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Threat create(@Valid @RequestBody ThreatRequest request) {
        return service.create(request);
    }

    @PutMapping("/{id}")
    public Threat update(@PathVariable Long id, @Valid @RequestBody ThreatRequest request) {
        return service.update(id, request);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long id) {
        service.delete(id);
    }
}
