package com.smartsplit.splitservice.Model;

import java.util.List;

import lombok.Data;

@Data
public class ReceiptItemSplit{
    private String itemName;
    private int totalPrice;
    private int quantity;
    private List<FriendSplit> friendSplits;
}