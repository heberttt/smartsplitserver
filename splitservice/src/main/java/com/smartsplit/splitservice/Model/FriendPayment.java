package com.smartsplit.splitservice.Model;

import lombok.Data;

@Data
public class FriendPayment {
    private Friend friend;
    private int totalDebt;
    private boolean hasPaid;
    private String paymentImageLink;
}
