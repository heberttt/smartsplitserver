package com.smartsplit.accountservice.Controller.Implementation;

import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.smartsplit.accountservice.Controller.PublicAccountController;
import com.smartsplit.accountservice.Result.GetAccountResult;
import com.smartsplit.accountservice.Service.AccountService;

@Controller
@RequestMapping("/public/account2")
public class PublicAccountControllerImpl implements PublicAccountController{

    final AccountService accountService;
    
    public PublicAccountControllerImpl(AccountService accountService) {
        this.accountService = accountService;
    }

    @GetMapping("")
    public ResponseEntity<GetAccountResult> getAccountById(@RequestParam String id) {
        final GetAccountResult result = accountService.getAccountById(id);

        return new ResponseEntity<>(result, HttpStatusCode.valueOf(result.getStatusCode()));
    }

}
