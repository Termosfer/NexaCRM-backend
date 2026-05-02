package com.nexacrm.api.dto;

import java.math.BigDecimal;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class MonthlyTrendDTO {
	private String month; // Məsələn: "Apr"
	private long customers; // Həmin ay gələn müştəri
	private long leads; // Həmin ay açılan satışlar
	private BigDecimal revenue; // Həmin ay qazanılan pul (WON olanlar)
}