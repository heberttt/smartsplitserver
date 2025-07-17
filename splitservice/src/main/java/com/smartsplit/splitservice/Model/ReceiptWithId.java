package com.smartsplit.splitservice.Model;

import java.util.List;

import lombok.Data;

@Data
public class ReceiptWithId {
    private int id;
    private Receipt receipt;
    private String creatorId;
    private List<FriendPayment> members;
    private String publicAccessToken;
    private String groupId;
}
