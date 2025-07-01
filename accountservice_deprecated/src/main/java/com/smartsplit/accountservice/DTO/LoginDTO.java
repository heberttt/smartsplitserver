package com.smartsplit.accountservice.DTO;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class LoginDTO {
    
    @Email(message = "Invalid email format")
    @NotNull
    private String email;
    
    @NotBlank(message = "password is required")
    @Size(min = 8, max = 64, message = "Password must be between 8 or 64 characters")
    private String password;

}
