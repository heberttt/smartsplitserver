package com.smartsplit.splitservice.Controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;

import com.smartsplit.splitservice.Result.AttachPaymentPublicResult;
import com.smartsplit.splitservice.Result.GetSplitBillWithTokenResult;

public interface PublicLinkController {
    public ResponseEntity<GetSplitBillWithTokenResult> getSplitBillWithToken(int billId, String token);


    public ResponseEntity<AttachPaymentPublicResult> attachPaymentPublic(MultipartFile file,int billId, String token,String guestName);
}
