package com.smartsplit.accountservice.Service;

import org.springframework.security.oauth2.jwt.Jwt;

import com.smartsplit.accountservice.Request.ChangeUsernameRequest;
import com.smartsplit.accountservice.Result.ChangeUsernameResult;
import com.smartsplit.accountservice.Result.LoginResult;

public interface AccountService {
    public LoginResult login(Jwt jwt);

    public ChangeUsernameResult changeUsername(ChangeUsernameRequest request, Jwt jwt);
}
