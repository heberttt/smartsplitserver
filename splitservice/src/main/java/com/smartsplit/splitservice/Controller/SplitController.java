package com.smartsplit.splitservice.Controller;

import org.springframework.http.ResponseEntity;
import org.springframework.security.oauth2.jwt.Jwt;

import com.smartsplit.splitservice.Request.CreateNewBillRequest;
import com.smartsplit.splitservice.Request.DeleteBillRequest;
import com.smartsplit.splitservice.Result.CreateNewBillResult;
import com.smartsplit.splitservice.Result.DeleteBillResult;
import com.smartsplit.splitservice.Result.GetMyBillsResult;

public interface SplitController {
    public ResponseEntity<CreateNewBillResult> createNewBill(CreateNewBillRequest request, Jwt jwt);

    public ResponseEntity<GetMyBillsResult> getMyReceipts(Jwt jwt);

    public ResponseEntity<DeleteBillResult> deleteBill(DeleteBillRequest request, Jwt jwt);
}
