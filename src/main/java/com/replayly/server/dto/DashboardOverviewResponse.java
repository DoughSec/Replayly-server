package com.replayly.server.dto;

import lombok.Builder;

import java.util.List;

@Builder
public record DashboardOverviewResponse(
        long totalCapturedFailures,
        long openIssues,
        long resolvedIssues,
        List<LabelCountResponse> mostCommonStatusCodes,
        List<MostReplayedResponse> mostReplayedFailures
) {
}
