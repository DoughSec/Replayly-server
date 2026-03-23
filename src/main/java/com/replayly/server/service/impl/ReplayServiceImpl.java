package com.replayly.server.service.impl;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.replayly.server.dto.ReplayAttemptResponse;
import com.replayly.server.dto.ReplayRequest;
import com.replayly.server.dto.RequestComparisonResponse;
import com.replayly.server.exception.BadRequestException;
import com.replayly.server.exception.ResourceNotFoundException;
import com.replayly.server.model.ApiRequestLog;
import com.replayly.server.model.ReplayAttempt;
import com.replayly.server.model.RequestComparison;
import com.replayly.server.repository.ApiRequestLogRepository;
import com.replayly.server.repository.ReplayAttemptRepository;
import com.replayly.server.repository.RequestComparisonRepository;
import com.replayly.server.service.ReplayService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.RestClientException;

import java.time.Duration;
import java.time.Instant;
import java.util.List;
import java.util.Map;
import java.util.Objects;

@Service
@RequiredArgsConstructor
@Transactional
public class ReplayServiceImpl implements ReplayService {

    private final ApiRequestLogRepository apiRequestLogRepository;
    private final ReplayAttemptRepository replayAttemptRepository;
    private final RequestComparisonRepository requestComparisonRepository;
    private final RestClient.Builder restClientBuilder;
    private final ObjectMapper objectMapper;

    @Override
    public ReplayAttemptResponse replay(Long apiRequestLogId, ReplayRequest request) {
        ApiRequestLog log = apiRequestLogRepository.findById(apiRequestLogId)
                .orElseThrow(() -> new ResourceNotFoundException("Request log not found: " + apiRequestLogId));

        String url = resolveUrl(log);
        HttpMethod method = resolveMethod(log.getMethod());
        HttpHeaders headers = parseHeaders(log.getRequestHeaders(), log.getContentType());

        Instant start = Instant.now();
        Integer status = null;
        String replayHeaders = null;
        String replayBody = null;
        String replayError = null;

        try {
            ResponseEntity<String> response = restClientBuilder.build()
                    .method(method)
                    .uri(url)
                    .headers(httpHeaders -> httpHeaders.addAll(headers))
                    .body(log.getRequestBody())
                    .retrieve()
                    .toEntity(String.class);
            status = response.getStatusCode().value();
            replayHeaders = response.getHeaders().toString();
            replayBody = response.getBody();
        } catch (RestClientException exception) {
            replayError = exception.getMessage();
        }

        RequestComparison comparison = buildComparison(log, status, replayBody, replayError);
        ReplayAttempt savedAttempt = replayAttemptRepository.save(ReplayAttempt.builder()
                .apiRequestLog(log)
                .replayedBy(request.getReplayedBy())
                .requestSnapshot(buildRequestSnapshot(log, url))
                .responseStatus(status)
                .responseHeaders(replayHeaders)
                .responseBody(replayBody)
                .errorMessage(replayError)
                .durationMs(Duration.between(start, Instant.now()).toMillis())
                .success(status != null && status >= 200 && status < 300)
                .comparisonSummary(comparison.getSummary())
                .build());

        comparison.setReplayAttempt(savedAttempt);
        savedAttempt.setComparison(requestComparisonRepository.save(comparison));
        return EntityMapper.toReplayResponse(savedAttempt);
    }

    @Override
    @Transactional(readOnly = true)
    public List<ReplayAttemptResponse> findByRequestLogId(Long apiRequestLogId) {
        return replayAttemptRepository.findByApiRequestLogIdOrderByReplayedAtDesc(apiRequestLogId).stream()
                .map(EntityMapper::toReplayResponse)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public ReplayAttemptResponse findById(Long replayId) {
        ReplayAttempt replayAttempt = replayAttemptRepository.findById(replayId)
                .orElseThrow(() -> new ResourceNotFoundException("Replay not found: " + replayId));
        return EntityMapper.toReplayResponse(replayAttempt);
    }

    @Override
    @Transactional(readOnly = true)
    public RequestComparisonResponse findComparison(Long replayId) {
        return requestComparisonRepository.findByReplayAttemptId(replayId)
                .map(EntityMapper::toComparisonResponse)
                .orElseThrow(() -> new ResourceNotFoundException("Comparison not found for replay: " + replayId));
    }

    private String resolveUrl(ApiRequestLog log) {
        if (log.getFullUrl() != null && !log.getFullUrl().isBlank()) {
            return log.getFullUrl();
        }
        if (log.getBaseUrl() != null && log.getEndpointPath() != null) {
            return log.getBaseUrl() + log.getEndpointPath();
        }
        throw new BadRequestException("Stored request does not contain a replayable URL");
    }

    private HttpMethod resolveMethod(String method) {
        try {
            return HttpMethod.valueOf(method.toUpperCase());
        } catch (IllegalArgumentException exception) {
            throw new BadRequestException("Unsupported HTTP method: " + method);
        }
    }

    private HttpHeaders parseHeaders(String rawHeaders, String contentType) {
        HttpHeaders headers = new HttpHeaders();
        if (contentType != null && !contentType.isBlank()) {
            headers.add(HttpHeaders.CONTENT_TYPE, contentType);
        }
        if (rawHeaders == null || rawHeaders.isBlank()) {
            return headers;
        }
        try {
            Map<String, Object> parsed = objectMapper.readValue(rawHeaders, new TypeReference<>() {});
            parsed.forEach((key, value) -> headers.add(key, String.valueOf(value)));
        } catch (Exception ignored) {
            headers.add("X-Replayly-Original-Headers", rawHeaders);
        }
        return headers;
    }

    private String buildRequestSnapshot(ApiRequestLog log, String url) {
        try {
            return objectMapper.writeValueAsString(Map.of(
                    "method", log.getMethod(),
                    "url", url,
                    "headers", log.getRequestHeaders(),
                    "body", log.getRequestBody()
            ));
        } catch (Exception exception) {
            return "{\"method\":\"" + log.getMethod() + "\",\"url\":\"" + url + "\"}";
        }
    }

    private RequestComparison buildComparison(ApiRequestLog log, Integer replayStatus, String replayBody, String replayError) {
        boolean statusChanged = !Objects.equals(log.getResponseStatus(), replayStatus);
        boolean responseChanged = !Objects.equals(log.getResponseBody(), replayBody);
        boolean errorChanged = !Objects.equals(log.getErrorMessage(), replayError);

        String summary = statusChanged
                ? "Original status " + log.getResponseStatus() + ", replay status " + replayStatus
                : "Replay status matched original response";
        String diffNotes = "responseChanged=" + responseChanged + ", errorChanged=" + errorChanged;

        return RequestComparison.builder()
                .originalStatus(log.getResponseStatus())
                .replayStatus(replayStatus)
                .statusChanged(statusChanged)
                .responseChanged(responseChanged)
                .errorChanged(errorChanged)
                .summary(summary)
                .diffNotes(diffNotes)
                .build();
    }
}
