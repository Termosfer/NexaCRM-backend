package com.nexacrm.api.service;

import com.nexacrm.api.dto.DashboardStatsDTO;
import com.nexacrm.api.dto.MonthlyTrendDTO;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import java.util.*;

@Service
public class AIService {

    @Value("${app.gemini.api.key}")
    private String apiKey;

    // Sənin curl əmrindəki işlək URL və Model adı:
    private final String GEMINI_URL = "https://generativelanguage.googleapis.com/v1beta/models/gemini-flash-latest:generateContent?key=";

    public String getBusinessInsight(DashboardStatsDTO stats, List<MonthlyTrendDTO> trends, String sector) {
        RestTemplate restTemplate = new RestTemplate();

        String trendInfo = (trends == null || trends.isEmpty()) ? "Məlumat hələ toplanmayıb" : trends.toString();

        // AI-a göndərilən tapşırıq
        String prompt = String.format(
        	    "Sən Nexa Intelligence-in baş analitikisən. Sahə: %s. " +
        	    "Məlumatlar: %d müştəri, %f%% gəlir artımı. " +
        	    "Trend: %s. " +
        	    "Zəhmət olmasa cavabı mütləq bu 3 başlıqla ver: " +
        	    "1. [ANALİZ] (Vəziyyətin qısa izahı) " +
        	    "2. [MƏSLƏHƏTLƏR] (3 dənə konkret addım) " +
        	    "3. [PROQNOZ] (Gələcək ay üçün gözlənti). " +
        	    "Cavabı yalnız Azərbaycan dilində professional tonda yaz.",
        	    sector, stats.getTotalCustomers(), stats.getRevenueGrowth(), trendInfo
        	);

        // Gemini-nin gözlədiyi JSON strukturu
        Map<String, Object> requestBody = Map.of(
            "contents", List.of(Map.of(
                "parts", List.of(Map.of("text", prompt))
            ))
        );

        try {
            // Sorğu göndərilir
            Map<String, Object> response = restTemplate.postForObject(GEMINI_URL + apiKey, requestBody, Map.class);
            
            // Cavabın daxilindən mətni süzüb çıxarırıq
            if (response != null && response.containsKey("candidates")) {
                List<?> candidates = (List<?>) response.get("candidates");
                if (!candidates.isEmpty()) {
                    Map<?, ?> firstCandidate = (Map<?, ?>) candidates.get(0);
                    Map<?, ?> content = (Map<?, ?>) firstCandidate.get("content");
                    List<?> parts = (List<?>) content.get("parts");
                    Map<?, ?> firstPart = (Map<?, ?>) parts.get(0);
                    return (String) firstPart.get("text");
                }
            }
            return "AI cavab hazırlaya bilmədi.";
        } catch (Exception e) {
            System.err.println("XƏTA: " + e.getMessage());
            return "AI xidməti ilə əlaqə kəsildi. Lütfən API açarınızı yoxlayın.";
        }
    }
}