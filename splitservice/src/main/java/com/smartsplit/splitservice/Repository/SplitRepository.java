package com.smartsplit.splitservice.Repository;
import java.util.List;
import java.util.Optional;

import com.smartsplit.splitservice.Model.Receipt;
import com.smartsplit.splitservice.Model.ReceiptWithId;

public interface SplitRepository {
    public void createSplitBill(String payerId, Receipt receipt);

    public List<ReceiptWithId> findReceiptsByPayerId(String payerId);

    public void deleteBill(int billId);

    public Optional<ReceiptWithId> findReceiptById(int billId);

    public List<ReceiptWithId> findReceiptsWhereUserIsParticipant(String accountId);

    public void attachPayment(int billId, String payerId, String paymentImageLink);
}
