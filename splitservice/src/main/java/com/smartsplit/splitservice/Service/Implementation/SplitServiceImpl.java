package com.smartsplit.splitservice.Service.Implementation;

import java.util.List;
import java.util.Optional;

import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.smartsplit.splitservice.Firebase.FirebaseStorageService;
import com.smartsplit.splitservice.Model.FriendPayment;
import com.smartsplit.splitservice.Model.ReceiptWithId;
import com.smartsplit.splitservice.Repository.SplitRepository;
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
import com.smartsplit.splitservice.Service.SplitService;

@Service
public class SplitServiceImpl implements SplitService {

    private final SplitRepository splitRepository;

    private final FirebaseStorageService firebaseStorageService;

    public SplitServiceImpl(SplitRepository splitRepository, FirebaseStorageService firebaseStorageService) {
        this.splitRepository = splitRepository;
        this.firebaseStorageService = firebaseStorageService;
    }

    @Override
    public CreateNewBillResult createNewBill(CreateNewBillRequest request, Jwt jwt) {
        String initiatorId = jwt.getClaimAsString("user_id");

        CreateNewBillResult result = new CreateNewBillResult();

        try {

            splitRepository.createSplitBill(initiatorId, request.getGroupId(), request.getReceipt());

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

        try {
            final List<ReceiptWithId> myReceipts = splitRepository.findReceiptsByPayerId(initiatorId);

            result.setSuccess(true);
            result.setStatusCode(200);
            result.setData(myReceipts);

            return result;
        } catch (Exception e) {
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
            ReceiptWithId receipt = splitRepository.findReceiptById(request.getId())
                    .orElseThrow(() -> new Exception("Bill doesn't exist"));

            if (!receipt.getCreatorId().equals(initiatorId)) {
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

    @Override
    public GetMyDebtsResult getMyDebts(Jwt jwt) {
        String initiatorId = jwt.getClaimAsString("user_id");

        GetMyDebtsResult result = new GetMyDebtsResult();

        try {
            final List<ReceiptWithId> myReceipts = splitRepository.findReceiptsWhereUserIsParticipant(initiatorId);

            result.setSuccess(true);
            result.setStatusCode(200);
            result.setData(myReceipts);

            return result;
        } catch (Exception e) {
            result.setSuccess(false);
            result.setErrorMessage(e.toString());
            result.setStatusCode(500);

            return result;
        }
    }

    @Override
    public PayMyDebtResult payMyDebt(PayMyDebtRequest request, Jwt jwt) {
        String initiatorId = jwt.getClaimAsString("user_id");

        PayMyDebtResult result = new PayMyDebtResult();

        try {
            splitRepository.attachPayment(request.getBillId(), initiatorId, request.getPaymentLink());

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
    public GetSplitBillWithTokenResult getSplitBillWithToken(int billId, String token) {
        GetSplitBillWithTokenResult result = new GetSplitBillWithTokenResult();

        try {
            Optional<ReceiptWithId> bill = splitRepository.findReceiptById(billId);

            if (bill.isEmpty()) {
                result.setSuccess(false);
                result.setErrorMessage("Split bill not found");
                result.setStatusCode(404);

                return result;
            }

            if (!bill.get().getPublicAccessToken().equals(token)) {
                result.setSuccess(false);
                result.setErrorMessage("Token is incorrect");
                result.setStatusCode(401);

                return result;
            }

            result.setSuccess(true);
            result.setData(bill.get());
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
    public AttachPaymentPublicResult attachPaymentPublic(MultipartFile image, int billId, String token,
            String guestName) {
        AttachPaymentPublicResult result = new AttachPaymentPublicResult();

        try {
            Optional<ReceiptWithId> bill = splitRepository.findReceiptById(billId);

            if (bill.isEmpty()) {
                result.setSuccess(false);
                result.setErrorMessage("Split bill not found");
                result.setStatusCode(404);

                return result;
            }

            if (!bill.get().getPublicAccessToken().equals(token)) {
                result.setSuccess(false);
                result.setErrorMessage("Token is incorrect");
                result.setStatusCode(401);

                return result;
            }

            Optional<FriendPayment> matchingGuest = bill.get().getMembers().stream()
                    .filter(fp -> fp.getFriend() != null &&
                            guestName.equals(fp.getFriend().getUsername()))
                    .findFirst();

            if (matchingGuest.isEmpty()) {
                result.setSuccess(false);
                result.setErrorMessage("Guest with that name does not exist");
                result.setStatusCode(404);
                return result;
            }

            if (matchingGuest.get().isHasPaid()) {
                result.setSuccess(false);
                result.setErrorMessage("This guest has already paid.");
                result.setStatusCode(400);
                return result;
            }

            final String imageLink = firebaseStorageService.uploadImage(image,
                    "payment/" + billId + "/" + guestName + ".jpg");

            splitRepository.attachPaymentGuest(billId, guestName, imageLink);

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

}
