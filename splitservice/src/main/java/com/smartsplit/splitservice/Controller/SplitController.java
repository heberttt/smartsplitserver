package com.smartsplit.splitservice.Controller;

import org.springframework.http.ResponseEntity;
import org.springframework.security.oauth2.jwt.Jwt;

import com.smartsplit.splitservice.Request.CreateNewBillRequest;
import com.smartsplit.splitservice.Result.CreateNewBillResult;

public interface SplitController {
    public ResponseEntity<CreateNewBillResult> createNewBill(CreateNewBillRequest request, Jwt jwt);

    // public GetAllBillsResult getAllBills(Jwt jwt);

    // public 
}
