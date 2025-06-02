package com.smartsplit.accountservice.Controller.Implementations;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.smartsplit.accountservice.Controller.AccountController;
import com.smartsplit.accountservice.DTO.RegisterDTO;
import com.smartsplit.accountservice.Results.RegisterResult;
import com.smartsplit.accountservice.Service.AccountService;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;


@RestController
@RequestMapping("/api/accounts")
public class AccountControllerImpl implements AccountController{
    
    private final AccountService accountService;

    public AccountControllerImpl(AccountService accountService){
        this.accountService = accountService;
    }

    @PostMapping("/register")
    public ResponseEntity<RegisterResult> register(@RequestBody RegisterDTO dto) {
        RegisterResult result = accountService.register(dto);

        if (result.getSuccess()){
            return new ResponseEntity<>(result, HttpStatus.CREATED);
        }else if(result.getMessage().equals("Account created successfully")){
            return new ResponseEntity<>(result, HttpStatus.CONFLICT);
        }

        return new ResponseEntity<>(result, HttpStatus.INTERNAL_SERVER_ERROR);
    }
    

}
