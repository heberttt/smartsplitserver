package com.smartsplit.splitservice.Result;

import com.smartsplit.splitservice.Model.ReceiptWithId;

import lombok.Data;

@Data
public class GetSplitBillWithTokenResult {
    private Boolean success = false;

    private int statusCode;

    private String errorMessage;

    private ReceiptWithId data;
}
