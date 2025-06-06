package com.smartsplit.authservice.Clients;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.smartsplit.authservice.Clients.Results.GetAccountResult;

@FeignClient(name="accountservice", url= "http://localhost:8081")
public interface AccountClient {
    
    @GetMapping(value = "/api/accounts", params = "email")
    GetAccountResult getAccountByEmail(@RequestParam String email);

}
