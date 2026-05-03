package com.nexacrm.api.controller;

import com.nexacrm.api.dto.DashboardStatsDTO;
import com.nexacrm.api.dto.LeadStatusChartDTO;
import com.nexacrm.api.dto.MonthlyTrendDTO;
import com.nexacrm.api.entity.Organization;
import com.nexacrm.api.repository.OrganizationRepository; // Mütləq import edilməlidir
import com.nexacrm.api.service.AIService;
import com.nexacrm.api.service.DashboardService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/dashboard")
@RequiredArgsConstructor // Bütün 'final' sahələri avtomatik konstruktora əlavə edir
@CrossOrigin(origins = "http://localhost:5173") // Front-end icazəsi
public class DashboardController {

    private final DashboardService dashboardService;
    private final AIService aiService; // @Autowired yerinə 'final' və constructor injection
    private final OrganizationRepository organizationRepository; // BAX, BU ÇATMIRDI

    // 1. Ümumi Statistika (Müştəri sayı, Lead sayı, Gəlir)
    @GetMapping("/stats/{orgId}")
    public ResponseEntity<DashboardStatsDTO> getStats(@PathVariable UUID orgId) {
        return ResponseEntity.ok(dashboardService.getStatsByOrg(orgId));
    }
    
    // 2. Status Bölgüsü (Dairəvi və ya Radar qrafiki üçün)
    @GetMapping("/charts/{orgId}")
    public ResponseEntity<List<LeadStatusChartDTO>> getChartData(@PathVariable UUID orgId) {
        return ResponseEntity.ok(dashboardService.getLeadChartData(orgId));
    }

    // 3. 12 Aylıq Trend (Line/Area Chart üçün)
    @GetMapping("/trends/{orgId}")
    public ResponseEntity<List<MonthlyTrendDTO>> getTrends(@PathVariable UUID orgId) {
        return ResponseEntity.ok(dashboardService.getMonthlyTrends(orgId));
    }

    // 4. AI BİZNES ANALİZİ (Google Gemini İnteqrasiyası)
    @GetMapping("/ai-analysis/{orgId}")
    public ResponseEntity<Map<String, String>> getAIAnalysis(@PathVariable UUID orgId) {
        // Mövcud rəqəmsal məlumatları toplayırıq
        DashboardStatsDTO stats = dashboardService.getStatsByOrg(orgId);
        List<MonthlyTrendDTO> trends = dashboardService.getMonthlyTrends(orgId);
        
        // Şirkətin sektorunu (Turizm, Kurs və s.) tapırıq ki, AI daha dəqiq cavab versin
        Organization org = organizationRepository.findById(orgId)
                .orElseThrow(() -> new RuntimeException("Şirkət tapılmadı"));

        // AIService-i çağırırıq
        String insight = aiService.getBusinessInsight(stats, trends, org.getBusinessSector());

        return ResponseEntity.ok(Map.of("insight", insight));
    }
}