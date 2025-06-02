package com.smartsplit.accountservice.DTO;

import jakarta.validation.constraints.Email;
import lombok.Data;

@Data
public class RegisterDTO{
    private String username;
    @Email(message = "Invalid email format")
    private String email;
    private String password;
    private Boolean signedInWithGoogle;
}