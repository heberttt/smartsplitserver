package com.smartsplit.authservice.Security;

import java.security.Key;
import java.util.Base64;
import java.util.Date;

import javax.crypto.spec.SecretKeySpec;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import jakarta.annotation.PostConstruct;

@Component
public class JwtGenerator {

    @Value("${JWT_SECRET_KEY}")
    private String secret;

    private Key key;

    @PostConstruct
    public void init() {
        this.key = new SecretKeySpec(Base64.getDecoder().decode(secret), "HmacSHA512");
    };

    public String generateToken(Authentication auth){
        String email = auth.getName();
        Date currentDate = new Date();
        Date expirationDate = new Date(currentDate.getTime() + 150000);


        String token = Jwts.builder()
                .setSubject(email)
                .setIssuedAt(currentDate)
                .setExpiration(expirationDate)
                .signWith(key, SignatureAlgorithm.HS512)
                .compact();

        System.out.println("New token: " + token);
        return token;
    }
}