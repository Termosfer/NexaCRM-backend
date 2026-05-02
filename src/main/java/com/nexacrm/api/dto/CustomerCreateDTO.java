package com.nexacrm.api.dto;

import lombok.Getter;
import lombok.Setter;
import java.util.UUID;

@Getter @Setter
public class CustomerCreateDTO {
    private String firstName;
    private String lastName;
    private String email;
    private String phone;
    private String companyName;
    private UUID organizationId; // Bizə yalnız ID lazımdır
}