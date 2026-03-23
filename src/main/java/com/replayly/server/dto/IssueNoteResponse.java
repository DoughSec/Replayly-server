package com.replayly.server.dto;

import lombok.Builder;

import java.time.LocalDateTime;

@Builder
public record IssueNoteResponse(
        Long id,
        Long apiRequestLogId,
        String noteText,
        LocalDateTime createdAt,
        String createdBy
) {
}
