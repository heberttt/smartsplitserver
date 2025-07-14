package com.smartsplit.splitservice.Controller;

import org.springframework.http.ResponseEntity;
import org.springframework.security.oauth2.jwt.Jwt;

import com.smartsplit.splitservice.Request.PayMyDebtRequest;
import com.smartsplit.splitservice.Result.GetMyDebtsResult;
import com.smartsplit.splitservice.Result.PayMyDebtResult;

public interface DebtController {
    public ResponseEntity<GetMyDebtsResult> getMyDebts(Jwt jwt);

    public ResponseEntity<PayMyDebtResult> payMyDebt(PayMyDebtRequest request, Jwt jwt);
}
