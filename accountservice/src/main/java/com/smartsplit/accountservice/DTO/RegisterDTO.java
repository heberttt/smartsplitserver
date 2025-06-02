package com.smartsplit.accountservice.DTO;

import lombok.Data;

@Data
public class RegisterDTO{
    private String username;
    private String email;
    private String password;
    private Boolean signedInWithGoogle;
}