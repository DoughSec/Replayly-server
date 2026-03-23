package com.replayly.server.service.impl;

import com.replayly.server.dto.ApiRequestLogDetailResponse;
import com.replayly.server.dto.ApiRequestLogSummaryResponse;
import com.replayly.server.dto.IssueNoteResponse;
import com.replayly.server.dto.ReplayAttemptResponse;
import com.replayly.server.dto.RequestComparisonResponse;
import com.replayly.server.dto.TagResponse;
import com.replayly.server.model.ApiRequestLog;
import com.replayly.server.model.IssueNote;
import com.replayly.server.model.ReplayAttempt;
import com.replayly.server.model.RequestComparison;
import com.replayly.server.model.RequestTag;

import java.util.Collections;
import java.util.Comparator;
import java.util.Set;
import java.util.stream.Collectors;

final class EntityMapper {

    private EntityMapper() {
    }

    static TagResponse toTagResponse(RequestTag tag) {
        return TagResponse.builder()
                .id(tag.getId())
                .name(tag.getName())
                .color(tag.getColor())
                .description(tag.getDescription())
                .build();
    }

    static IssueNoteResponse toNoteResponse(IssueNote note) {
        return IssueNoteResponse.builder()
                .id(note.getId())
                .apiRequestLogId(note.getApiRequestLog().getId())
                .noteText(note.getNoteText())
                .createdAt(note.getCreatedAt())
                .createdBy(note.getCreatedBy())
                .build();
    }

    static RequestComparisonResponse toComparisonResponse(RequestComparison comparison) {
        if (comparison == null) {
            return null;
        }
        return RequestComparisonResponse.builder()
                .id(comparison.getId())
                .replayAttemptId(comparison.getReplayAttempt().getId())
                .originalStatus(comparison.getOriginalStatus())
                .replayStatus(comparison.getReplayStatus())
                .statusChanged(comparison.isStatusChanged())
                .responseChanged(comparison.isResponseChanged())
                .errorChanged(comparison.isErrorChanged())
                .summary(comparison.getSummary())
                .diffNotes(comparison.getDiffNotes())
                .build();
    }

    static ReplayAttemptResponse toReplayResponse(ReplayAttempt replayAttempt) {
        return ReplayAttemptResponse.builder()
                .id(replayAttempt.getId())
                .apiRequestLogId(replayAttempt.getApiRequestLog().getId())
                .replayedAt(replayAttempt.getReplayedAt())
                .replayedBy(replayAttempt.getReplayedBy())
                .requestSnapshot(replayAttempt.getRequestSnapshot())
                .responseStatus(replayAttempt.getResponseStatus())
                .responseHeaders(replayAttempt.getResponseHeaders())
                .responseBody(replayAttempt.getResponseBody())
                .errorMessage(replayAttempt.getErrorMessage())
                .durationMs(replayAttempt.getDurationMs())
                .success(replayAttempt.isSuccess())
                .comparisonSummary(replayAttempt.getComparisonSummary())
                .comparison(toComparisonResponse(replayAttempt.getComparison()))
                .build();
    }

    static ApiRequestLogSummaryResponse toSummaryResponse(ApiRequestLog log) {
        return ApiRequestLogSummaryResponse.builder()
                .id(log.getId())
                .requestName(log.getRequestName())
                .method(log.getMethod())
                .endpointPath(log.getEndpointPath())
                .responseStatus(log.getResponseStatus())
                .environment(log.getEnvironment())
                .status(log.getStatus())
                .severity(log.getSeverity())
                .capturedAt(log.getCapturedAt())
                .tags(toTagResponses(log.getTags()))
                .build();
    }

    static ApiRequestLogDetailResponse toDetailResponse(ApiRequestLog log) {
        return ApiRequestLogDetailResponse.builder()
                .id(log.getId())
                .requestName(log.getRequestName())
                .method(log.getMethod())
                .baseUrl(log.getBaseUrl())
                .endpointPath(log.getEndpointPath())
                .fullUrl(log.getFullUrl())
                .requestHeaders(log.getRequestHeaders())
                .queryParams(log.getQueryParams())
                .pathParams(log.getPathParams())
                .requestBody(log.getRequestBody())
                .contentType(log.getContentType())
                .responseStatus(log.getResponseStatus())
                .responseHeaders(log.getResponseHeaders())
                .responseBody(log.getResponseBody())
                .errorMessage(log.getErrorMessage())
                .sourceApplication(log.getSourceApplication())
                .environment(log.getEnvironment())
                .capturedAt(log.getCapturedAt())
                .createdBy(log.getCreatedBy())
                .status(log.getStatus())
                .severity(log.getSeverity())
                .tags(toTagResponses(log.getTags()))
                .notes(log.getNotes() == null ? Collections.emptyList() : log.getNotes().stream()
                        .sorted(Comparator.comparing(IssueNote::getCreatedAt).reversed())
                        .map(EntityMapper::toNoteResponse)
                        .toList())
                .replays(log.getReplayAttempts() == null ? Collections.emptyList() : log.getReplayAttempts().stream()
                        .sorted(Comparator.comparing(ReplayAttempt::getReplayedAt).reversed())
                        .map(EntityMapper::toReplayResponse)
                        .toList())
                .build();
    }

    private static Set<TagResponse> toTagResponses(Set<RequestTag> tags) {
        if (tags == null) {
            return Collections.emptySet();
        }
        return tags.stream()
                .map(EntityMapper::toTagResponse)
                .collect(Collectors.toCollection(java.util.LinkedHashSet::new));
    }
}
