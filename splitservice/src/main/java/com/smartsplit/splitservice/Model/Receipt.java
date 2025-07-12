package com.smartsplit.splitservice.Model;

import java.time.LocalDateTime;
import java.util.List;

import lombok.Data;

@Data
public class Receipt {
    private String name;
    private int additionalChargesPercent;
    private int roundingAdjustment;
    private LocalDateTime now = LocalDateTime.now();
    private List<ReceiptItemSplit> splits;
}
