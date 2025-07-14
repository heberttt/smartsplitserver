package com.smartsplit.splitservice.Controller.Implementation;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import com.smartsplit.splitservice.Controller.DebtController;
import com.smartsplit.splitservice.Request.PayMyDebtRequest;
import com.smartsplit.splitservice.Result.GetMyDebtsResult;
import com.smartsplit.splitservice.Result.PayMyDebtResult;
import com.smartsplit.splitservice.Service.SplitService;

@Controller
@RequestMapping("/api/debts")
public class DebtControllerImpl implements DebtController{

    private SplitService splitService;

    public DebtControllerImpl(SplitService splitService) {
        this.splitService = splitService;
    }

    @GetMapping()
    public ResponseEntity<GetMyDebtsResult> getMyDebts(@AuthenticationPrincipal Jwt jwt) {
        final GetMyDebtsResult result = splitService.getMyDebts(jwt);

        return new ResponseEntity<>(result, HttpStatus.valueOf(result.getStatusCode()));
    }

    @PostMapping()
    public ResponseEntity<PayMyDebtResult> payMyDebt(@RequestBody PayMyDebtRequest request, @AuthenticationPrincipal Jwt jwt) {
        final PayMyDebtResult result = splitService.payMyDebt(request, jwt);

        return new ResponseEntity<>(result, HttpStatus.valueOf(result.getStatusCode()));
    }

    
    
}
