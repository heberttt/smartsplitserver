package com.smartsplit.authservice.Models;

import lombok.Data;

@Data
public class AccountDO{
    private Integer id;
    private String username;
    private String email;
    private String passwordHash;
    private Boolean signedInWithGoogle;
}