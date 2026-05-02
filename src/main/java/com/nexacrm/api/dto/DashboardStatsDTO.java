package com.nexacrm.api.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import java.math.BigDecimal;

@Getter
@Setter
@AllArgsConstructor
public class DashboardStatsDTO {
    private long totalCustomers;
    private double customerGrowth; // Faizlə artım (məs: 12.5)
    
    private long totalLeads;
    private double leadsGrowth;
    
    private BigDecimal totalExpectedRevenue;
    private double revenueGrowth;
}