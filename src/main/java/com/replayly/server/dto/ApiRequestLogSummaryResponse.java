package com.replayly.server.dto;

import com.replayly.server.model.enums.RequestLogStatus;
import com.replayly.server.model.enums.SeverityLevel;
import lombok.Builder;

import java.time.LocalDateTime;
import java.util.Set;

@Builder
public record ApiRequestLogSummaryResponse(
        Long id,
        String requestName,
        String method,
        String endpointPath,
        Integer responseStatus,
        String environment,
        RequestLogStatus status,
        SeverityLevel severity,
        LocalDateTime capturedAt,
        Set<TagResponse> tags
) {
}
