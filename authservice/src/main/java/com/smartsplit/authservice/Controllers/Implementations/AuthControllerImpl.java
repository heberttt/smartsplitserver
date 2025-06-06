package com.smartsplit.authservice.Controllers.Implementations;

import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.smartsplit.authservice.Controllers.AuthController;
import com.smartsplit.authservice.DTO.JwtRequestDTO;
import com.smartsplit.authservice.Results.JwtResult;
import com.smartsplit.authservice.Services.AuthService;

@RestController
@RequestMapping("/api/auth")
public class AuthControllerImpl implements AuthController{

    private AuthService authService;

    public AuthControllerImpl(AuthService authService){
        this.authService = authService;
    }

    
    @PostMapping("/token")
    public ResponseEntity<JwtResult> getJwtToken(@RequestBody JwtRequestDTO dto){
        
        JwtResult result = authService.getJwtToken(dto);
        
        return new ResponseEntity<>(result, HttpStatusCode.valueOf(result.getStatusCode()));
    }


}   
