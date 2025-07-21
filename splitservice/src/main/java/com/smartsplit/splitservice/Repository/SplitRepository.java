package com.smartsplit.splitservice.Repository;
import java.util.List;
import java.util.Optional;

import com.smartsplit.splitservice.Model.Receipt;
import com.smartsplit.splitservice.Model.SplitBill;

public interface SplitRepository {
    public void createSplitBill(String payerId, String groupId, Receipt receipt);

    public List<SplitBill> findReceiptsByPayerId(String payerId);

    public void deleteBill(int billId);

    public Optional<SplitBill> findReceiptById(int billId);

    public List<SplitBill> findReceiptsWhereUserIsParticipant(String accountId);

    public void attachPayment(int billId, String payerId, String paymentImageLink);

    public void attachPaymentGuest(int billId, String guestName, String paymentImageLink);
}
