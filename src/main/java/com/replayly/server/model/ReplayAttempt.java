package com.replayly.server.model;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.Lob;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToOne;
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
@Table(name = "replay_attempts")
public class ReplayAttempt {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "api_request_log_id", nullable = false)
    private ApiRequestLog apiRequestLog;

    @Column(name = "replayed_at", nullable = false)
    private LocalDateTime replayedAt;

    @Column(name = "replayed_by")
    private String replayedBy;

    @Lob
    @Column(name = "request_snapshot", columnDefinition = "LONGTEXT")
    private String requestSnapshot;

    @Column(name = "response_status")
    private Integer responseStatus;

    @Lob
    @Column(name = "response_headers", columnDefinition = "LONGTEXT")
    private String responseHeaders;

    @Lob
    @Column(name = "response_body", columnDefinition = "LONGTEXT")
    private String responseBody;

    @Lob
    @Column(name = "error_message", columnDefinition = "LONGTEXT")
    private String errorMessage;

    @Column(name = "duration_ms")
    private Long durationMs;

    @Column(nullable = false)
    private boolean success;

    @Lob
    @Column(name = "comparison_summary", columnDefinition = "LONGTEXT")
    private String comparisonSummary;

    @OneToOne(mappedBy = "replayAttempt", cascade = CascadeType.ALL, orphanRemoval = true)
    private RequestComparison comparison;

    @PrePersist
    public void prePersist() {
        if (replayedAt == null) {
            replayedAt = LocalDateTime.now();
        }
    }
}
