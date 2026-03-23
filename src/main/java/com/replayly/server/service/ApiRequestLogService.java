package com.replayly.server.service;

import com.replayly.server.dto.ApiRequestLogCreateRequest;
import com.replayly.server.dto.ApiRequestLogDetailResponse;
import com.replayly.server.dto.ApiRequestLogSummaryResponse;
import com.replayly.server.dto.ApiRequestLogUpdateRequest;
import com.replayly.server.model.enums.RequestLogStatus;
import com.replayly.server.model.enums.SeverityLevel;

import java.time.LocalDate;
import java.util.List;

public interface ApiRequestLogService {
    ApiRequestLogDetailResponse create(ApiRequestLogCreateRequest request);

    List<ApiRequestLogSummaryResponse> findAll(
            RequestLogStatus status,
            SeverityLevel severity,
            String environment,
            String method,
            String endpoint,
            String tag,
            LocalDate startDate,
            LocalDate endDate
    );

    ApiRequestLogDetailResponse findById(Long id);

    ApiRequestLogDetailResponse update(Long id, ApiRequestLogUpdateRequest request);

    void delete(Long id);

    ApiRequestLogDetailResponse attachTag(Long id, Long tagId);

    ApiRequestLogDetailResponse removeTag(Long id, Long tagId);
}
