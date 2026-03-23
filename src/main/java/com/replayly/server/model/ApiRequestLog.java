package com.replayly.server.model;

import com.replayly.server.model.enums.RequestLogStatus;
import com.replayly.server.model.enums.SeverityLevel;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.Lob;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.OneToMany;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

@AllArgsConstructor
@Builder
@Data
@Entity
@NoArgsConstructor
@Table(name = "api_request_logs")
public class ApiRequestLog {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "request_name", nullable = false)
    private String requestName;

    @Column(nullable = false, length = 16)
    private String method;

    @Column(name = "base_url")
    private String baseUrl;

    @Column(name = "endpoint_path")
    private String endpointPath;

    @Lob
    @Column(name = "full_url")
    private String fullUrl;

    @Lob
    @Column(name = "request_headers", columnDefinition = "LONGTEXT")
    private String requestHeaders;

    @Lob
    @Column(name = "query_params", columnDefinition = "LONGTEXT")
    private String queryParams;

    @Lob
    @Column(name = "path_params", columnDefinition = "LONGTEXT")
    private String pathParams;

    @Lob
    @Column(name = "request_body", columnDefinition = "LONGTEXT")
    private String requestBody;

    @Column(name = "content_type")
    private String contentType;

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

    @Column(name = "source_application")
    private String sourceApplication;

    @Column(nullable = false)
    private String environment;

    @Column(name = "captured_at", nullable = false)
    private LocalDateTime capturedAt;

    @Column(name = "created_by")
    private String createdBy;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private RequestLogStatus status;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private SeverityLevel severity;

    @OneToMany(mappedBy = "apiRequestLog", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ReplayAttempt> replayAttempts;

    @OneToMany(mappedBy = "apiRequestLog", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<IssueNote> notes;

    @OneToMany(mappedBy = "apiRequestLog", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ReplayConfiguration> replayConfigurations;

    @OneToMany(mappedBy = "apiRequestLog", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ResolutionStatusHistory> statusHistory;

    @Builder.Default
    @ManyToMany
    @JoinTable(
            name = "api_request_log_tags",
            joinColumns = @JoinColumn(name = "api_request_log_id"),
            inverseJoinColumns = @JoinColumn(name = "request_tag_id")
    )
    private Set<RequestTag> tags = new LinkedHashSet<>();

    @PrePersist
    public void prePersist() {
        if (capturedAt == null) {
            capturedAt = LocalDateTime.now();
        }
        if (status == null) {
            status = RequestLogStatus.OPEN;
        }
        if (severity == null) {
            severity = SeverityLevel.MEDIUM;
        }
    }
}
