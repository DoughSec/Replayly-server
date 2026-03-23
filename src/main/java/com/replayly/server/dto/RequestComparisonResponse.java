package com.replayly.server.dto;

import lombok.Builder;

@Builder
public record RequestComparisonResponse(
        Long id,
        Long replayAttemptId,
        Integer originalStatus,
        Integer replayStatus,
        boolean statusChanged,
        boolean responseChanged,
        boolean errorChanged,
        String summary,
        String diffNotes
) {
}
