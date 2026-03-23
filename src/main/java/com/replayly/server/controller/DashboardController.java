package com.replayly.server.controller;

import com.replayly.server.dto.DashboardOverviewResponse;
import com.replayly.server.dto.LabelCountResponse;
import com.replayly.server.service.DashboardService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/dashboard")
public class DashboardController {

    private final DashboardService dashboardService;

    @GetMapping("/overview")
    public DashboardOverviewResponse overview() {
        return dashboardService.getOverview();
    }

    @GetMapping("/status-summary")
    public List<LabelCountResponse> statusSummary() {
        return dashboardService.getStatusSummary();
    }

    @GetMapping("/severity-summary")
    public List<LabelCountResponse> severitySummary() {
        return dashboardService.getSeveritySummary();
    }

    @GetMapping("/endpoints-summary")
    public List<LabelCountResponse> endpointsSummary() {
        return dashboardService.getEndpointsSummary();
    }
}
