package com.smartsplit.splitservice.Controller;

import org.springframework.http.ResponseEntity;

import com.smartsplit.splitservice.Result.GetSplitBillWithTokenResult;

public interface PublicLinkController {
    public ResponseEntity<GetSplitBillWithTokenResult> getSplitBillWithToken(int billId, String token);
}
