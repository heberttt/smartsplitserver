package com.smartsplit.splitservice.Result;

import java.util.List;

import com.smartsplit.splitservice.Model.ReceiptWithId;

import lombok.Data;

@Data
public class GetMyDebtsResult {
    private Boolean success = false;

    private int statusCode;

    private String errorMessage;

    private List<ReceiptWithId> data;
}
