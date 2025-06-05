package com.smartsplit.authservice.Controllers;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;

import com.smartsplit.authservice.DTO.JwtRequestDTO;
import com.smartsplit.authservice.Results.JwtResult;

public interface AuthController {
    public ResponseEntity<JwtResult> getJwtToken(@RequestBody JwtRequestDTO dto);

}
