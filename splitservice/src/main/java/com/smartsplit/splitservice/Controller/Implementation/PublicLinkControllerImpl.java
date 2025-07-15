package com.smartsplit.splitservice.Controller.Implementation;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.smartsplit.splitservice.Controller.PublicLinkController;
import com.smartsplit.splitservice.Result.GetSplitBillWithTokenResult;
import com.smartsplit.splitservice.Service.SplitService;

@Controller
@RequestMapping("/public/splitbill")
public class PublicLinkControllerImpl implements PublicLinkController{

    private SplitService splitService;

    public PublicLinkControllerImpl(SplitService splitService) {
        this.splitService = splitService;
    }

    @GetMapping()
    public ResponseEntity<GetSplitBillWithTokenResult> getSplitBillWithToken(@RequestParam int billId, @RequestParam String token) {
        final GetSplitBillWithTokenResult result = splitService.getSplitBillWithToken(billId, token);

        return new ResponseEntity<>(result, HttpStatus.valueOf(result.getStatusCode()));
    }
    
}
