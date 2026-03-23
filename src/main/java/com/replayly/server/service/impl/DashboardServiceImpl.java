package com.replayly.server.service.impl;

import com.replayly.server.dto.DashboardOverviewResponse;
import com.replayly.server.dto.LabelCountResponse;
import com.replayly.server.dto.MostReplayedResponse;
import com.replayly.server.model.enums.RequestLogStatus;
import com.replayly.server.repository.ApiRequestLogRepository;
import com.replayly.server.repository.ReplayAttemptRepository;
import com.replayly.server.service.DashboardService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class DashboardServiceImpl implements DashboardService {

    private final ApiRequestLogRepository apiRequestLogRepository;
    private final ReplayAttemptRepository replayAttemptRepository;

    @Override
    public DashboardOverviewResponse getOverview() {
        return DashboardOverviewResponse.builder()
                .totalCapturedFailures(apiRequestLogRepository.count())
                .openIssues(apiRequestLogRepository.countByStatus(RequestLogStatus.OPEN))
                .resolvedIssues(apiRequestLogRepository.countByStatus(RequestLogStatus.RESOLVED))
                .mostCommonStatusCodes(apiRequestLogRepository.countGroupedByResponseStatus().stream()
                        .limit(5)
                        .map(view -> LabelCountResponse.builder().label(view.getLabel()).total(view.getTotal()).build())
                        .toList())
                .mostReplayedFailures(replayAttemptRepository.findMostReplayed().stream()
                        .limit(5)
                        .map(view -> MostReplayedResponse.builder()
                                .apiRequestLogId(view.getApiRequestLogId())
                                .requestName(view.getRequestName())
                                .replayCount(view.getReplayCount())
                                .build())
                        .toList())
                .build();
    }

    @Override
    public List<LabelCountResponse> getStatusSummary() {
        return apiRequestLogRepository.countGroupedByStatus().stream()
                .map(view -> LabelCountResponse.builder().label(view.getLabel()).total(view.getTotal()).build())
                .toList();
    }

    @Override
    public List<LabelCountResponse> getSeveritySummary() {
        return apiRequestLogRepository.countGroupedBySeverity().stream()
                .map(view -> LabelCountResponse.builder().label(view.getLabel()).total(view.getTotal()).build())
                .toList();
    }

    @Override
    public List<LabelCountResponse> getEndpointsSummary() {
        return apiRequestLogRepository.countGroupedByEndpoint().stream()
                .limit(10)
                .map(view -> LabelCountResponse.builder().label(view.getLabel()).total(view.getTotal()).build())
                .toList();
    }
}
