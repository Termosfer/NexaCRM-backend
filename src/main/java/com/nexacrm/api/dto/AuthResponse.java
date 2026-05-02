package com.nexacrm.api.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import java.util.UUID;

@Getter @Setter @AllArgsConstructor
public class AuthResponse {
    private String token;
    private String email;
    private String role;
    private UUID organizationId;
    private String companyName;
}