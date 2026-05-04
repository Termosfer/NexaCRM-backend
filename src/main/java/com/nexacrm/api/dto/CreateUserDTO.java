package com.nexacrm.api.dto;

import com.nexacrm.api.entity.Role;
import lombok.Getter;
import lombok.Setter;
import java.math.BigDecimal;
import java.util.UUID;

@Getter @Setter
public class CreateUserDTO {
    private String name;
    private String email;
    private String password;
    private Role role; // MANAGER və ya USER
    private String jobTitle; // Məsələn: Senior Agent
    private BigDecimal salary;
    private BigDecimal bonusAmount;
    private UUID departmentId; // İsteğə bağlı (Opsional)
    private UUID organizationId; 
}