package com.replayly.server.dto;

import lombok.Builder;

@Builder
public record MostReplayedResponse(
        Long apiRequestLogId,
        String requestName,
        long replayCount
) {
}
