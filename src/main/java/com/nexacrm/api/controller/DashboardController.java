package com.nexacrm.api.controller;


import com.nexacrm.api.dto.DashboardStatsDTO;
import com.nexacrm.api.dto.LeadStatusChartDTO;
import com.nexacrm.api.dto.MonthlyTrendDTO;
import com.nexacrm.api.service.DashboardService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/dashboard")
@RequiredArgsConstructor
public class DashboardController {

    private final DashboardService dashboardService;

    // Şirkətin ID-sini göndəririk, bizə statistika qayıdır
    @GetMapping("/stats/{orgId}")
    public ResponseEntity<DashboardStatsDTO> getStats(@PathVariable UUID orgId) {
        return ResponseEntity.ok(dashboardService.getStatsByOrg(orgId));
    }
    
    @GetMapping("/charts/{orgId}")
    public ResponseEntity<List<LeadStatusChartDTO>> getChartData(@PathVariable UUID orgId) {
        return ResponseEntity.ok(dashboardService.getLeadChartData(orgId));
    }
    @GetMapping("/trends/{orgId}")
    public ResponseEntity<List<MonthlyTrendDTO>> getTrends(@PathVariable UUID orgId) {
        return ResponseEntity.ok(dashboardService.getMonthlyTrends(orgId));
    }
}