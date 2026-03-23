package com.replayly.server.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.Lob;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@AllArgsConstructor
@Builder
@Data
@Entity
@NoArgsConstructor
@Table(name = "replay_configurations")
public class ReplayConfiguration {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "api_request_log_id", nullable = false)
    private ApiRequestLog apiRequestLog;

    @Lob
    @Column(name = "override_headers", columnDefinition = "LONGTEXT")
    private String overrideHeaders;

    @Lob
    @Column(name = "override_body", columnDefinition = "LONGTEXT")
    private String overrideBody;

    @Column(name = "override_base_url")
    private String overrideBaseUrl;

    @Column(name = "timeout_ms")
    private Integer timeoutMs;

    @Column(name = "follow_redirects")
    private Boolean followRedirects;

    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;

    @PrePersist
    public void prePersist() {
        if (createdAt == null) {
            createdAt = LocalDateTime.now();
        }
    }
}
