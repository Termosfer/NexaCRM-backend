package com.nexacrm.api.service;

import com.nexacrm.api.dto.DashboardStatsDTO;
import com.nexacrm.api.dto.LeadStatusChartDTO;
import com.nexacrm.api.dto.MonthlyTrendDTO;
import com.nexacrm.api.entity.Lead;
import com.nexacrm.api.repository.CustomerRepository;
import com.nexacrm.api.repository.LeadRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.time.format.TextStyle;
import java.time.temporal.TemporalAdjusters;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class DashboardService {

    private final CustomerRepository customerRepository;
    private final LeadRepository leadRepository;

    public DashboardStatsDTO getStatsByOrg(UUID orgId) {
        try {
            LocalDateTime now = LocalDateTime.now();
            LocalDateTime startOfThisMonth = now.with(TemporalAdjusters.firstDayOfMonth()).withHour(0).withMinute(0).withSecond(0);
            LocalDateTime startOfLastMonth = startOfThisMonth.minusMonths(1);
            LocalDateTime endOfLastMonth = startOfThisMonth.minusNanos(1);

            long currentMonthCustomers = customerRepository.countByOrganizationIdAndCreatedAtBetween(orgId, startOfThisMonth, now);
            long lastMonthCustomers = customerRepository.countByOrganizationIdAndCreatedAtBetween(orgId, startOfLastMonth, endOfLastMonth);
            double customerGrowth = calculateGrowth(currentMonthCustomers, lastMonthCustomers);

            long currentMonthLeads = leadRepository.countByOrganizationIdAndCreatedAtBetween(orgId, startOfThisMonth, now);
            long lastMonthLeads = leadRepository.countByOrganizationIdAndCreatedAtBetween(orgId, startOfLastMonth, endOfLastMonth);
            double leadsGrowth = calculateGrowth(currentMonthLeads, lastMonthLeads);

            BigDecimal currentMonthRevenue = leadRepository.sumAmountByOrganizationIdAndCreatedAtBetween(orgId, startOfThisMonth, now);
            BigDecimal lastMonthRevenue = leadRepository.sumAmountByOrganizationIdAndCreatedAtBetween(orgId, startOfLastMonth, endOfLastMonth);
            
            double revenueGrowth = calculateRevenueGrowth(
                currentMonthRevenue != null ? currentMonthRevenue : BigDecimal.ZERO, 
                lastMonthRevenue != null ? lastMonthRevenue : BigDecimal.ZERO
            );

            // Bütün bazadakı ümumi statistikalar
            List<Lead> allLeads = leadRepository.findByOrganizationIdOrderByCreatedAtDesc(orgId);
            long totalCustomers = customerRepository.findByOrganizationIdOrderByCreatedAtDesc(orgId).size();
            long totalLeads = allLeads.size();
            
            BigDecimal totalRevenue = allLeads.stream()
                    .filter(l -> l.getStatus() != null && l.getStatus().toString().equals("WON"))
                    .map(l -> l.getAmount() != null ? l.getAmount() : BigDecimal.ZERO)
                    .reduce(BigDecimal.ZERO, BigDecimal::add);

            return new DashboardStatsDTO(totalCustomers, customerGrowth, totalLeads, leadsGrowth, totalRevenue, revenueGrowth);
        } catch (Exception e) {
            e.printStackTrace(); // Xətanın tam səbəbini konsolda görmək üçün
            throw new RuntimeException("Stats hesablama xətası: " + e.getMessage());
        }
    }

    public List<MonthlyTrendDTO> getMonthlyTrends(UUID orgId) {
        List<MonthlyTrendDTO> trends = new ArrayList<>();
        LocalDateTime now = LocalDateTime.now();
        List<Lead> allLeads = leadRepository.findByOrganizationIdOrderByCreatedAtDesc(orgId);

        for (int i = 11; i >= 0; i--) {
            LocalDateTime monthStart = now.minusMonths(i).with(TemporalAdjusters.firstDayOfMonth()).withHour(0).withMinute(0);
            LocalDateTime monthEnd = monthStart.with(TemporalAdjusters.lastDayOfMonth()).withHour(23).withMinute(59);
            // Locale-i ehtiyat üçün default edirik
            String monthName = monthStart.getMonth().getDisplayName(TextStyle.SHORT, Locale.getDefault());

            long custCount = customerRepository.countByOrganizationIdAndCreatedAtBetween(orgId, monthStart, monthEnd);
            
            long leadCount = allLeads.stream()
                    .filter(l -> l.getCreatedAt() != null && l.getCreatedAt().isAfter(monthStart) && l.getCreatedAt().isBefore(monthEnd))
                    .count();

            BigDecimal rev = allLeads.stream()
                    .filter(l -> l.getStatus() != null && l.getStatus().toString().equals("WON") 
                            && l.getCreatedAt() != null 
                            && l.getCreatedAt().isAfter(monthStart) 
                            && l.getCreatedAt().isBefore(monthEnd))
                    .map(l -> l.getAmount() != null ? l.getAmount() : BigDecimal.ZERO)
                    .reduce(BigDecimal.ZERO, BigDecimal::add);

            trends.add(new MonthlyTrendDTO(monthName, custCount, leadCount, rev));
        }
        return trends;
    }

    public List<LeadStatusChartDTO> getLeadChartData(UUID orgId) {
        return leadRepository.findByOrganizationIdOrderByCreatedAtDesc(orgId).stream()
                .filter(l -> l.getStatus() != null)
                .collect(Collectors.groupingBy(l -> l.getStatus().toString(), Collectors.counting()))
                .entrySet().stream()
                .map(e -> new LeadStatusChartDTO(e.getKey(), e.getValue()))
                .collect(Collectors.toList());
    }

    private double calculateGrowth(long current, long previous) {
        if (previous <= 0) return current > 0 ? 100.0 : 0.0;
        return ((double) (current - previous) / previous) * 100;
    }

    private double calculateRevenueGrowth(BigDecimal current, BigDecimal previous) {
        if (previous == null || previous.compareTo(BigDecimal.ZERO) <= 0) {
            return (current != null && current.compareTo(BigDecimal.ZERO) > 0) ? 100.0 : 0.0;
        }
        return current.subtract(previous)
                .divide(previous, 4, RoundingMode.HALF_UP)
                .multiply(BigDecimal.valueOf(100))
                .doubleValue();
    }
}