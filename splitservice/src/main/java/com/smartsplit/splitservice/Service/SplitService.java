package com.smartsplit.splitservice.Service;

import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.multipart.MultipartFile;

import com.smartsplit.splitservice.Request.CreateNewBillRequest;
import com.smartsplit.splitservice.Request.DeleteBillRequest;
import com.smartsplit.splitservice.Request.PayMyDebtRequest;
import com.smartsplit.splitservice.Result.AttachPaymentPublicResult;
import com.smartsplit.splitservice.Result.CreateNewBillResult;
import com.smartsplit.splitservice.Result.DeleteBillResult;
import com.smartsplit.splitservice.Result.GetMyBillsResult;
import com.smartsplit.splitservice.Result.GetMyDebtsResult;
import com.smartsplit.splitservice.Result.GetSplitBillWithTokenResult;
import com.smartsplit.splitservice.Result.PayMyDebtResult;

public interface SplitService {
    public CreateNewBillResult createNewBill(CreateNewBillRequest request, Jwt jwt);

    public GetMyBillsResult getMyBills(Jwt jwt);

    public DeleteBillResult deleteBill(DeleteBillRequest request, Jwt jwt);

    public GetMyDebtsResult getMyDebts(Jwt jwt);

    public PayMyDebtResult payMyDebt(PayMyDebtRequest request, Jwt jwt);

    public GetSplitBillWithTokenResult getSplitBillWithToken(int billId, String token);

    public AttachPaymentPublicResult attachPaymentPublic(MultipartFile image, int billId, String token, String guestName);
}
