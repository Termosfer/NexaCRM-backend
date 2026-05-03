package com.nexacrm.api.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class ResetPasswordDTO {
    
    @NotBlank
    private String token; // Email-dən gələn unikal kod

    @NotBlank
    @Size(min = 8, message = "Yeni şifrə ən azı 8 simvol olmalıdır")
    private String newPassword;
}