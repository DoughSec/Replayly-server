package com.replayly.server.dto;

import com.replayly.server.model.enums.RequestLogStatus;
import com.replayly.server.model.enums.SeverityLevel;
import lombok.Builder;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

@Builder
public record ApiRequestLogDetailResponse(
        Long id,
        String requestName,
        String method,
        String baseUrl,
        String endpointPath,
        String fullUrl,
        String requestHeaders,
        String queryParams,
        String pathParams,
        String requestBody,
        String contentType,
        Integer responseStatus,
        String responseHeaders,
        String responseBody,
        String errorMessage,
        String sourceApplication,
        String environment,
        LocalDateTime capturedAt,
        String createdBy,
        RequestLogStatus status,
        SeverityLevel severity,
        Set<TagResponse> tags,
        List<IssueNoteResponse> notes,
        List<ReplayAttemptResponse> replays
) {
}
