package com.replayly.server.service.impl;

import com.replayly.server.dto.ApiRequestLogCreateRequest;
import com.replayly.server.dto.ApiRequestLogDetailResponse;
import com.replayly.server.dto.ApiRequestLogSummaryResponse;
import com.replayly.server.dto.ApiRequestLogUpdateRequest;
import com.replayly.server.exception.ResourceNotFoundException;
import com.replayly.server.model.ApiRequestLog;
import com.replayly.server.model.RequestTag;
import com.replayly.server.model.ResolutionStatusHistory;
import com.replayly.server.model.enums.RequestLogStatus;
import com.replayly.server.model.enums.SeverityLevel;
import com.replayly.server.repository.ApiRequestLogRepository;
import com.replayly.server.repository.RequestTagRepository;
import com.replayly.server.repository.ResolutionStatusHistoryRepository;
import com.replayly.server.service.ApiRequestLogService;
import jakarta.persistence.criteria.JoinType;
import lombok.RequiredArgsConstructor;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

@Service
@RequiredArgsConstructor
@Transactional
public class ApiRequestLogServiceImpl implements ApiRequestLogService {

    private final ApiRequestLogRepository apiRequestLogRepository;
    private final RequestTagRepository requestTagRepository;
    private final ResolutionStatusHistoryRepository resolutionStatusHistoryRepository;

    @Override
    public ApiRequestLogDetailResponse create(ApiRequestLogCreateRequest request) {
        ApiRequestLog log = ApiRequestLog.builder()
                .requestName(request.getRequestName())
                .method(request.getMethod())
                .baseUrl(request.getBaseUrl())
                .endpointPath(request.getEndpointPath())
                .fullUrl(request.getFullUrl())
                .requestHeaders(request.getRequestHeaders())
                .queryParams(request.getQueryParams())
                .pathParams(request.getPathParams())
                .requestBody(request.getRequestBody())
                .contentType(request.getContentType())
                .responseStatus(request.getResponseStatus())
                .responseHeaders(request.getResponseHeaders())
                .responseBody(request.getResponseBody())
                .errorMessage(request.getErrorMessage())
                .sourceApplication(request.getSourceApplication())
                .environment(request.getEnvironment())
                .capturedAt(request.getCapturedAt())
                .createdBy(request.getCreatedBy())
                .status(request.getStatus())
                .severity(request.getSeverity())
                .tags(resolveTags(request.getTagIds()))
                .build();

        ApiRequestLog saved = apiRequestLogRepository.save(log);
        resolutionStatusHistoryRepository.save(ResolutionStatusHistory.builder()
                .apiRequestLog(saved)
                .newStatus(saved.getStatus())
                .changedBy(saved.getCreatedBy())
                .reason("Initial capture")
                .build());
        return findById(saved.getId());
    }

    @Override
    @Transactional(readOnly = true)
    public List<ApiRequestLogSummaryResponse> findAll(RequestLogStatus status, SeverityLevel severity, String environment, String method, String endpoint, String tag, LocalDate startDate, LocalDate endDate) {
        Specification<ApiRequestLog> specification = (root, query, cb) -> cb.conjunction();

        if (status != null) {
            specification = specification.and((root, query, cb) -> cb.equal(root.get("status"), status));
        }
        if (severity != null) {
            specification = specification.and((root, query, cb) -> cb.equal(root.get("severity"), severity));
        }
        if (environment != null && !environment.isBlank()) {
            specification = specification.and((root, query, cb) -> cb.equal(cb.lower(root.get("environment")), environment.toLowerCase()));
        }
        if (method != null && !method.isBlank()) {
            specification = specification.and((root, query, cb) -> cb.equal(cb.upper(root.get("method")), method.toUpperCase()));
        }
        if (endpoint != null && !endpoint.isBlank()) {
            specification = specification.and((root, query, cb) -> cb.like(cb.lower(root.get("endpointPath")), "%" + endpoint.toLowerCase() + "%"));
        }
        if (tag != null && !tag.isBlank()) {
            specification = specification.and((root, query, cb) -> {
                query.distinct(true);
                return cb.equal(cb.lower(root.join("tags", JoinType.LEFT).get("name")), tag.toLowerCase());
            });
        }
        if (startDate != null) {
            specification = specification.and((root, query, cb) -> cb.greaterThanOrEqualTo(root.get("capturedAt"), startDate.atStartOfDay()));
        }
        if (endDate != null) {
            LocalDateTime endOfDay = endDate.plusDays(1).atStartOfDay().minusNanos(1);
            specification = specification.and((root, query, cb) -> cb.lessThanOrEqualTo(root.get("capturedAt"), endOfDay));
        }

        return apiRequestLogRepository.findAll(specification).stream()
                .map(EntityMapper::toSummaryResponse)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public ApiRequestLogDetailResponse findById(Long id) {
        return EntityMapper.toDetailResponse(getEntity(id));
    }

    @Override
    public ApiRequestLogDetailResponse update(Long id, ApiRequestLogUpdateRequest request) {
        ApiRequestLog log = getEntity(id);
        RequestLogStatus previousStatus = log.getStatus();

        if (request.getRequestName() != null && !request.getRequestName().isBlank()) {
            log.setRequestName(request.getRequestName());
        }
        if (request.getSeverity() != null) {
            log.setSeverity(request.getSeverity());
        }
        if (request.getStatus() != null) {
            log.setStatus(request.getStatus());
        }
        if (request.getTagIds() != null) {
            log.setTags(resolveTags(request.getTagIds()));
        }

        ApiRequestLog saved = apiRequestLogRepository.save(log);
        if (request.getStatus() != null && previousStatus != request.getStatus()) {
            resolutionStatusHistoryRepository.save(ResolutionStatusHistory.builder()
                    .apiRequestLog(saved)
                    .oldStatus(previousStatus)
                    .newStatus(request.getStatus())
                    .changedBy(request.getChangedBy())
                    .reason(request.getReason())
                    .build());
        }
        return findById(saved.getId());
    }

    @Override
    public void delete(Long id) {
        apiRequestLogRepository.delete(getEntity(id));
    }

    @Override
    public ApiRequestLogDetailResponse attachTag(Long id, Long tagId) {
        ApiRequestLog log = getEntity(id);
        RequestTag tag = requestTagRepository.findById(tagId)
                .orElseThrow(() -> new ResourceNotFoundException("Tag not found: " + tagId));
        log.getTags().add(tag);
        apiRequestLogRepository.save(log);
        return findById(id);
    }

    @Override
    public ApiRequestLogDetailResponse removeTag(Long id, Long tagId) {
        ApiRequestLog log = getEntity(id);
        log.getTags().removeIf(tag -> tag.getId().equals(tagId));
        apiRequestLogRepository.save(log);
        return findById(id);
    }

    private ApiRequestLog getEntity(Long id) {
        return apiRequestLogRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Request log not found: " + id));
    }

    private Set<RequestTag> resolveTags(Set<Long> tagIds) {
        if (tagIds == null || tagIds.isEmpty()) {
            return new LinkedHashSet<>();
        }
        List<RequestTag> tags = requestTagRepository.findAllById(tagIds);
        if (tags.size() != tagIds.size()) {
            throw new ResourceNotFoundException("One or more tags were not found");
        }
        return new LinkedHashSet<>(tags);
    }
}
