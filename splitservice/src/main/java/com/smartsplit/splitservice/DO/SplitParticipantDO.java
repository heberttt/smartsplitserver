package com.smartsplit.splitservice.DO;

import java.time.LocalDateTime;

import lombok.Data;

@Data
public class SplitParticipantDO {
    private Integer id;
    private Integer billId;
    private String accountId;
    private String guestName;
    private Boolean isPaid;
    private LocalDateTime paidAt;
    private String paymentProofLink;
}