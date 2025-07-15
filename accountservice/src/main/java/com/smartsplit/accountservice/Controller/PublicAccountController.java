package com.smartsplit.accountservice.Controller;

import org.springframework.http.ResponseEntity;

import com.smartsplit.accountservice.Result.GetAccountResult;

public interface PublicAccountController {
    public ResponseEntity<GetAccountResult> getAccountById(String id);
}
