package com.replayly.server.dto;

import lombok.Builder;

import java.time.LocalDateTime;

@Builder
public record ReplayAttemptResponse(
        Long id,
        Long apiRequestLogId,
        LocalDateTime replayedAt,
        String replayedBy,
        String requestSnapshot,
        Integer responseStatus,
        String responseHeaders,
        String responseBody,
        String errorMessage,
        Long durationMs,
        boolean success,
        String comparisonSummary,
        RequestComparisonResponse comparison
) {
}
