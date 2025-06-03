package com.smartsplit.accountservice.Controller;

import org.springframework.http.ResponseEntity;

import com.smartsplit.accountservice.DTO.RegisterDTO;
import com.smartsplit.accountservice.Results.RegisterResult;

public interface AccountController {
    public ResponseEntity<RegisterResult> register(RegisterDTO dto);
}
