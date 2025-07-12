package com.smartsplit.splitservice.Model;

import java.time.LocalDateTime;

import lombok.Data;

@Data
public class FriendPayment {
    private Friend friend;
    private int totalDebt;
    private boolean hasPaid;
    private LocalDateTime paidAt;
    private String paymentImageLink;
}
