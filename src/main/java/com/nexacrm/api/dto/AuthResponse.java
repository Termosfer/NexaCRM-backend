package com.nexacrm.api.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import java.util.UUID;

@Getter @Setter @AllArgsConstructor
public class AuthResponse {
    private String token;
    private UUID id;             // <--- BU SƏTİRİ ƏLAVƏ ETDİK (İstifadəçinin öz ID-si)
    private String email;
    private String name;         // <--- BU SƏTİRİ DA ƏLAVƏ EDƏK (Dashboard-da adı göstərmək üçün)
    private String role;
    private UUID organizationId;
    private String companyName;
    private String businessSector;
}