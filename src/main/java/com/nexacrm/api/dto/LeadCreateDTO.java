package com.nexacrm.api.dto;

import com.nexacrm.api.entity.LeadStatus;
import lombok.Getter;
import lombok.Setter;
import java.math.BigDecimal;
import java.util.UUID;

@Getter @Setter
public class LeadCreateDTO {
    private String title;
    private String description;
    private BigDecimal amount;
    private LeadStatus status;
    private UUID customerId;      // Obyekt yox, yalnız ID
    private UUID organizationId;  // Obyekt yox, yalnız ID
}