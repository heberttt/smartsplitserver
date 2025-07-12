package com.smartsplit.splitservice.Controller.Implementation;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import com.smartsplit.splitservice.Controller.SplitController;
import com.smartsplit.splitservice.Request.CreateNewBillRequest;
import com.smartsplit.splitservice.Result.CreateNewBillResult;
import com.smartsplit.splitservice.Result.GetMyBillsResult;
import com.smartsplit.splitservice.Service.SplitService;
import org.springframework.web.bind.annotation.GetMapping;


@Controller
@RequestMapping("/api/splits")
public class SplitControllerImpl implements SplitController{

    private SplitService splitService;

    public SplitControllerImpl(SplitService splitService){
        this.splitService = splitService;
    }

    @PostMapping()
    public ResponseEntity<CreateNewBillResult> createNewBill(@RequestBody CreateNewBillRequest request, @AuthenticationPrincipal Jwt jwt) {
        final CreateNewBillResult result = splitService.createNewBill(request, jwt);

        return new ResponseEntity<>(result, HttpStatus.valueOf(result.getStatusCode()));
    }

    @GetMapping()
    public ResponseEntity<GetMyBillsResult> getMyReceipts(@AuthenticationPrincipal Jwt jwt) {
        final GetMyBillsResult result = splitService.getMyBills(jwt);

        return new ResponseEntity<>(result, HttpStatus.valueOf(result.getStatusCode()));
    }

    
    
    
}
