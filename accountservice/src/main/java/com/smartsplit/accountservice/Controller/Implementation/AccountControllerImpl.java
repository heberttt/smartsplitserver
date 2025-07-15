package com.smartsplit.accountservice.Controller.Implementation;

import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.smartsplit.accountservice.Controller.AccountController;
import com.smartsplit.accountservice.Request.ChangeProfilePictureRequest;
import com.smartsplit.accountservice.Request.ChangeUsernameRequest;
import com.smartsplit.accountservice.Request.GetAccountsRequest;
import com.smartsplit.accountservice.Result.ChangeProfilePictureResult;
import com.smartsplit.accountservice.Result.ChangeUsernameResult;
import com.smartsplit.accountservice.Result.GetAccountsResult;
import com.smartsplit.accountservice.Result.LoginResult;
import com.smartsplit.accountservice.Service.AccountService;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;


@RestController
@RequestMapping("/api/accounts")
public class AccountControllerImpl implements AccountController {

    final AccountService accountService;

    public AccountControllerImpl(AccountService accountService){
        this.accountService = accountService;
    }

    @PostMapping("/login")    
    public ResponseEntity<LoginResult> login(@AuthenticationPrincipal Jwt jwt) {
        final LoginResult result = accountService.login(jwt);

        return new ResponseEntity<>(result, HttpStatus.valueOf(result.getStatusCode()));
    }

    @PutMapping("/username")
    public ResponseEntity<ChangeUsernameResult> changeUsername(@RequestBody ChangeUsernameRequest request, @AuthenticationPrincipal Jwt jwt) {
        final ChangeUsernameResult result = accountService.changeUsername(request, jwt);

        return new ResponseEntity<>(result, HttpStatusCode.valueOf(result.getStatusCode()));
    }

    @PutMapping("/profilePictureLink")
    public ResponseEntity<ChangeProfilePictureResult> changeProfilePicture(@RequestBody ChangeProfilePictureRequest request, @AuthenticationPrincipal Jwt jwt) {
        final ChangeProfilePictureResult result = accountService.changeProfilePicture(request, jwt);

        return new ResponseEntity<>(result, HttpStatusCode.valueOf(result.getStatusCode()));
    }

    @PostMapping("")
    public ResponseEntity<GetAccountsResult> getAccountsById(@RequestBody GetAccountsRequest request) {
        final GetAccountsResult result = accountService.getAccountsById(request);

        return new ResponseEntity<>(result, HttpStatusCode.valueOf(result.getStatusCode()));
    }
    

    

    // @Override
    // public ResponseEntity<GetAccountResult> getAccount(@RequestBody GetAccountRequest request,@AuthenticationPrincipal Jwt jwt) {
    //     final GetAccountResult result = accountService.getAccount(request, jwt);

    //     return new ResponseEntity<>(result, HttpStatusCode.valueOf(result.getStatusCode()));
    // }   

    
    
}
