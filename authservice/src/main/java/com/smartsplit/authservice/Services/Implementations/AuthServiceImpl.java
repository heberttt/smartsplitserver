package com.smartsplit.authservice.Services.Implementations;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import com.smartsplit.authservice.DTO.JwtRequestDTO;
import com.smartsplit.authservice.Results.JwtResult;
import com.smartsplit.authservice.Security.JwtGenerator;
import com.smartsplit.authservice.Services.AuthService;

@Service
public class AuthServiceImpl implements AuthService{

    private AuthenticationManager authenticationManager;
    private JwtGenerator jwtGenerator;

    public AuthServiceImpl(AuthenticationManager authenticationManager, JwtGenerator jwtGenerator){
        this.authenticationManager = authenticationManager;
        this.jwtGenerator = jwtGenerator;
    }

    @Override
    public JwtResult getJwtToken(JwtRequestDTO dto) {
        Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(dto.getEmail(), dto.getPassword()));
        SecurityContextHolder.getContext().setAuthentication(authentication);

        String token = jwtGenerator.generateToken(authentication);

        JwtResult result = new JwtResult();

        result.setSuccess(true);
        result.setJwtToken(token);
        result.setStatusCode(200);

        return result;
    }
    
}
