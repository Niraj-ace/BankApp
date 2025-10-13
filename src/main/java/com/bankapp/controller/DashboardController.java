package com.bankapp.controller;

import com.bankapp.dto.DashboardSummaryDTO;
import com.bankapp.service.DashboardService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/dashboard")
public class DashboardController {

    private final DashboardService dashboardService;

    public DashboardController(DashboardService dashboardService) {
        this.dashboardService = dashboardService;
    }

    @GetMapping("/{accountId}")
    public DashboardSummaryDTO getDashboard(@PathVariable Long accountId) {
        return dashboardService.getDashboardSummary(accountId);
    }
}
