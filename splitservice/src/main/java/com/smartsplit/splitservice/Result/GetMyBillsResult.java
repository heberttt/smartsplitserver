package com.smartsplit.splitservice.Result;

import java.util.List;

import com.smartsplit.splitservice.Model.Receipt;

import lombok.Data;

@Data
public class GetMyBillsResult {
    private Boolean success = false;

    private int statusCode;

    private String errorMessage;

    private List<Receipt> data;
}
