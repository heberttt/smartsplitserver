package com.smartsplit.accountservice.Model;

import jakarta.validation.constraints.Email;
import lombok.Data;

@Data
public class AccountDO{
    private Integer id;
    private String username;
    private String email;
    private String passwordHash;
    private Boolean signedInWithGoogle;
}