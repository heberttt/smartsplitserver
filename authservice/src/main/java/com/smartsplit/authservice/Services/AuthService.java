package com.smartsplit.authservice.Services;

import com.smartsplit.authservice.DTO.JwtRequestDTO;
import com.smartsplit.authservice.Results.JwtResult;

public interface AuthService {
    public JwtResult getJwtToken(JwtRequestDTO dto);
}
