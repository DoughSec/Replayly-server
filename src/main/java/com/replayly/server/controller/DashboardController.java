package com.replayly.server.controller;

import com.replayly.server.dto.DashboardOverviewResponse;
import com.replayly.server.dto.LabelCountResponse;
import com.replayly.server.service.DashboardService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
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
    @PreAuthorize("hasAnyRole('VIEWER', 'DEVELOPER', 'ADMIN')")
    public DashboardOverviewResponse overview() {
        return dashboardService.getOverview();
    }

    @GetMapping("/status-summary")
    @PreAuthorize("hasAnyRole('VIEWER', 'DEVELOPER', 'ADMIN')")
    public List<LabelCountResponse> statusSummary() {
        return dashboardService.getStatusSummary();
    }

    @GetMapping("/severity-summary")
    @PreAuthorize("hasAnyRole('VIEWER', 'DEVELOPER', 'ADMIN')")
    public List<LabelCountResponse> severitySummary() {
        return dashboardService.getSeveritySummary();
    }

    @GetMapping("/endpoints-summary")
    @PreAuthorize("hasAnyRole('VIEWER', 'DEVELOPER', 'ADMIN')")
    public List<LabelCountResponse> endpointsSummary() {
        return dashboardService.getEndpointsSummary();
    }
}
