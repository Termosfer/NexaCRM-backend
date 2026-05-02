package com.nexacrm.api.dto;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class CustomerUpdateDTO {
    private String firstName;
    private String lastName;
    private String email;
    private String phone;
    private String companyName;
}