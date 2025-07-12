package com.smartsplit.splitservice.Service.Implementation;

import java.util.List;

import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Service;

import com.smartsplit.splitservice.Model.ReceiptWithId;
import com.smartsplit.splitservice.Repository.SplitRepository;
import com.smartsplit.splitservice.Request.CreateNewBillRequest;
import com.smartsplit.splitservice.Request.DeleteBillRequest;
import com.smartsplit.splitservice.Result.CreateNewBillResult;
import com.smartsplit.splitservice.Result.DeleteBillResult;
import com.smartsplit.splitservice.Result.GetMyBillsResult;
import com.smartsplit.splitservice.Service.SplitService;

@Service
public class SplitServiceImpl implements SplitService {

    private final SplitRepository splitRepository;

    public SplitServiceImpl(SplitRepository splitRepository){
        this.splitRepository = splitRepository;
    }

    @Override
    public CreateNewBillResult createNewBill(CreateNewBillRequest request, Jwt jwt) {
        String initiatorId = jwt.getClaimAsString("user_id");

        CreateNewBillResult result = new CreateNewBillResult();

        try {

            splitRepository.createSplitBill(initiatorId, request.getReceipt());

            result.setSuccess(true);
            result.setStatusCode(200);
            
            return result;
        } catch (Exception e) {
            result.setSuccess(false);
            result.setErrorMessage(e.toString());
            result.setStatusCode(500);

            return result;
        }
    }

    @Override
    public GetMyBillsResult getMyBills(Jwt jwt) {
        String initiatorId = jwt.getClaimAsString("user_id");

        GetMyBillsResult result = new GetMyBillsResult();

        try{
            final List<ReceiptWithId> myReceipts = splitRepository.findReceiptsByPayerId(initiatorId);

            result.setSuccess(true);
            result.setStatusCode(200);
            result.setData(myReceipts);

            return result;
        }catch(Exception e){
            result.setSuccess(false);
            result.setErrorMessage(e.toString());
            result.setStatusCode(500);

            return result;
        }
    }

    @Override
    public DeleteBillResult deleteBill(DeleteBillRequest request, Jwt jwt) {
        String initiatorId = jwt.getClaimAsString("user_id");

        DeleteBillResult result = new DeleteBillResult();

        try {
            ReceiptWithId receipt = splitRepository.findReceiptById(request.getId()).orElseThrow(() -> new Exception("Bill doesn't exist"));
            
            if (!receipt.getCreatorId().equals(initiatorId)){
                result.setSuccess(false);
                result.setErrorMessage("You are not the owner of the receipt");
                result.setStatusCode(401);
                return result;
            }

            splitRepository.deleteBill(request.getId());

            result.setStatusCode(200);
            result.setSuccess(true);
            
            return result;
        } catch (Exception e) {
            result.setSuccess(false);
            result.setErrorMessage(e.toString());
            result.setStatusCode(500);

            return result;
        }
    }

}
