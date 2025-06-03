package com.smartsplit.accountservice.Controller.Implementations;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.smartsplit.accountservice.Controller.AccountController;
import com.smartsplit.accountservice.DTO.LoginDTO;
import com.smartsplit.accountservice.DTO.RegisterDTO;
import com.smartsplit.accountservice.Results.GetAccountResult;
import com.smartsplit.accountservice.Results.LoginResult;
import com.smartsplit.accountservice.Results.RegisterResult;
import com.smartsplit.accountservice.Service.AccountService;

import jakarta.validation.Valid;

import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;


@RestController
@RequestMapping("/api/accounts")
public class AccountControllerImpl implements AccountController{
    
    private final AccountService accountService;

    public AccountControllerImpl(AccountService accountService){
        this.accountService = accountService;
    }

    @PostMapping("/register")
    public ResponseEntity<RegisterResult> register(@Valid @RequestBody RegisterDTO dto) {
        RegisterResult result = accountService.register(dto);

        // if (result.getSuccess()){
        //     return new ResponseEntity<>(result, HttpStatus.CREATED);
        // }else if(result.getErrorMessage().equals("Email already registered")){
        //     return new ResponseEntity<>(result, HttpStatus.CONFLICT);
        // }

        return new ResponseEntity<>(result, HttpStatusCode.valueOf(result.getStatusCode()));
    }

    @GetMapping(params="email")
    public ResponseEntity<GetAccountResult> getAccountByEmail(@RequestParam String email) {
        GetAccountResult result = accountService.getAccountByEmail(email);

        return new ResponseEntity<>(result, HttpStatusCode.valueOf(result.getStatusCode()));
    }
    

    

}
