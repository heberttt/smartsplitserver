package com.smartsplit.splitservice.Request;

import lombok.Data;

@Data
public class PayMyDebtRequest {
    private int billId;

    private String paymentLink;
}
