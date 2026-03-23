package com.replayly.server.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.Lob;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@Builder
@Data
@Entity
@NoArgsConstructor
@Table(name = "request_comparisons")
public class RequestComparison {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "replay_attempt_id", nullable = false, unique = true)
    private ReplayAttempt replayAttempt;

    @Column(name = "original_status")
    private Integer originalStatus;

    @Column(name = "replay_status")
    private Integer replayStatus;

    @Column(name = "status_changed", nullable = false)
    private boolean statusChanged;

    @Column(name = "response_changed", nullable = false)
    private boolean responseChanged;

    @Column(name = "error_changed", nullable = false)
    private boolean errorChanged;

    @Column(nullable = false)
    private String summary;

    @Lob
    @Column(name = "diff_notes", columnDefinition = "LONGTEXT")
    private String diffNotes;
}
