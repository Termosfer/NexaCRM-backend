package com.nexacrm.api.dto;

import jakarta.validation.constraints.*;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class RegisterRequest {

    @NotBlank(message = "Ad və Soyad boş qala bilməz")
    private String fullName;

    @NotBlank(message = "Email boş qala bilməz")
    @Email(message = "Düzgün email ünvanı daxil edin")
    private String email;

    @NotBlank(message = "Şifrə boş qala bilməz")
    @Size(min = 8, message = "Şifrə ən azı 8 simvol olmalıdır")
    @Pattern(regexp = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=!])(?=\\S+$).{8,}$", 
             message = "Şifrədə ən azı bir rəqəm, bir böyük hərf və bir xüsusi işarə (@#$%^&+=!) olmalıdır")
    private String password;

    @NotBlank(message = "Şirkət adı boş qala bilməz")
    private String companyName;

    @NotBlank(message = "Biznes sahəsi seçilməlidir")
    private String businessSector;
}