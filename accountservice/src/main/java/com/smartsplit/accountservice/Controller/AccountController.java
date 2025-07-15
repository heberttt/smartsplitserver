package com.smartsplit.accountservice.Controller;

import org.springframework.http.ResponseEntity;
import org.springframework.security.oauth2.jwt.Jwt;

import com.smartsplit.accountservice.Request.ChangeProfilePictureRequest;
import com.smartsplit.accountservice.Request.ChangeUsernameRequest;
import com.smartsplit.accountservice.Result.ChangeProfilePictureResult;
import com.smartsplit.accountservice.Result.ChangeUsernameResult;
import com.smartsplit.accountservice.Result.LoginResult;

public interface AccountController {
    // public ResponseEntity<GetAccountResult> getAccount(Jwt jwt);
    
    public ResponseEntity<LoginResult> login(Jwt jwt);

    public ResponseEntity<ChangeUsernameResult> changeUsername(ChangeUsernameRequest request, Jwt jwt);

    public ResponseEntity<ChangeProfilePictureResult> changeProfilePicture(ChangeProfilePictureRequest request, Jwt jwt);
    
}
