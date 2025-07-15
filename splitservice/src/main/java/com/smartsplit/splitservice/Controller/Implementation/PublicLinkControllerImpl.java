package com.smartsplit.splitservice.Controller.Implementation;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import com.smartsplit.splitservice.Controller.PublicLinkController;
import com.smartsplit.splitservice.Result.AttachPaymentPublicResult;
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

    @PostMapping()
    public ResponseEntity<AttachPaymentPublicResult> attachPaymentPublic(@RequestParam("file") MultipartFile file, @RequestParam int billId, @RequestParam String token, @RequestParam String guestName){
        final AttachPaymentPublicResult result = splitService.attachPaymentPublic(file, billId, token, guestName);
        System.out.println(guestName);
        return new ResponseEntity<>(result, HttpStatus.valueOf(result.getStatusCode()));
    }
    
}
