package com.threatintel.repository;

import com.threatintel.model.Severity;
import com.threatintel.model.Status;
import com.threatintel.model.Threat;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface ThreatRepository extends JpaRepository<Threat, Long>, JpaSpecificationExecutor<Threat> {
    long countBySeverity(Severity severity);
    long countByStatus(Status status);
}
