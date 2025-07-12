package com.smartsplit.splitservice.Repository;
import java.util.List;

import com.smartsplit.splitservice.Model.Receipt;

public interface SplitRepository {
    public void createSplitBill(String payerId, Receipt receipt);

    public List<Receipt> findReceiptsById(String payerId);
}
