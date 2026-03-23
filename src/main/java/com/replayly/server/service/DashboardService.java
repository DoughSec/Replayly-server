package com.replayly.server.service;

import com.replayly.server.dto.DashboardOverviewResponse;
import com.replayly.server.dto.LabelCountResponse;

import java.util.List;

public interface DashboardService {
    DashboardOverviewResponse getOverview();
    List<LabelCountResponse> getStatusSummary();
    List<LabelCountResponse> getSeveritySummary();
    List<LabelCountResponse> getEndpointsSummary();
}
