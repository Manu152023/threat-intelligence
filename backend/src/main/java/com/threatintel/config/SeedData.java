package com.threatintel.config;

import com.threatintel.model.Category;
import com.threatintel.model.Severity;
import com.threatintel.model.Status;
import com.threatintel.model.Threat;
import com.threatintel.repository.ThreatRepository;
import java.util.List;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/** Inserts sample data only when the table is empty, so restarts never duplicate rows. */
@Configuration
public class SeedData {

    @Bean
    CommandLineRunner seed(ThreatRepository repository) {
        return args -> {
            if (repository.count() > 0) return;
            repository.saveAll(List.of(
                    threat("Emotet banking trojan campaign", "New Emotet variant spreading through malicious Office attachments.", Severity.HIGH, Category.MALWARE, Status.ACTIVE, "CERT-EU"),
                    threat("Phishing wave impersonating payroll portals", "Credential-harvesting emails targeting HR departments.", Severity.MEDIUM, Category.PHISHING, Status.INVESTIGATING, "CISA"),
                    threat("LockBit ransomware affiliate activity", "Targeted attacks on healthcare providers using stolen VPN credentials.", Severity.CRITICAL, Category.RANSOMWARE, Status.ACTIVE, "FBI"),
                    threat("Volumetric DDoS against financial APIs", "Short, high-bandwidth bursts causing intermittent latency.", Severity.HIGH, Category.DDOS, Status.MITIGATED, "Cloudflare"),
                    threat("Remote code execution in a popular web framework", "Unauthenticated RCE via deserialization; patch available.", Severity.CRITICAL, Category.VULNERABILITY, Status.INVESTIGATING, "NVD"),
                    threat("Customer database exposed via misconfigured bucket", "Public cloud storage bucket leaked contact records.", Severity.HIGH, Category.DATA_BREACH, Status.RESOLVED, "Internal"),
                    threat("Fake software update lures", "Malicious browser pop-ups distributing info-stealers.", Severity.LOW, Category.MALWARE, Status.RESOLVED, "CERT-IN"),
                    threat("Credential stuffing against login endpoints", "High-volume login attempts using leaked password lists.", Severity.MEDIUM, Category.OTHER, Status.ACTIVE, "Internal")));
        };
    }

    private Threat threat(String title, String description, Severity severity, Category category, Status status, String source) {
        Threat t = new Threat();
        t.setTitle(title);
        t.setDescription(description);
        t.setSeverity(severity);
        t.setCategory(category);
        t.setStatus(status);
        t.setSource(source);
        return t;
    }
}
