package com.nexacrm.api.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class LeadStatusChartDTO {
    private String name; // Statusun adı (məs: NEW)
    private long value;  // O statusdakı lead-lərin sayı
}