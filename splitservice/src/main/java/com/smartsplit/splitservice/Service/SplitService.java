package com.smartsplit.splitservice.Service;

import org.springframework.security.oauth2.jwt.Jwt;

import com.smartsplit.splitservice.Request.CreateNewBillRequest;
import com.smartsplit.splitservice.Result.CreateNewBillResult;
import com.smartsplit.splitservice.Result.GetMyBillsResult;

public interface SplitService {
    public CreateNewBillResult createNewBill(CreateNewBillRequest request, Jwt jwt);

    public GetMyBillsResult getMyBills(Jwt jwt);
}
