package com.nexacrm.api.dto;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class RegisterRequest {
    private String fullName;
    private String email;
    private String password;
    private String companyName; // Bu sətir avtomatik Organization yaradacaq
}