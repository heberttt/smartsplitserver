package com.smartsplit.authservice.DTO;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import lombok.Data;


@Data
public class JwtRequestDTO {
    @Email(message = "Invalid email format")
    @NotNull
    private String email;
    
    @NotBlank(message = "password is required")
    private String password;
}
