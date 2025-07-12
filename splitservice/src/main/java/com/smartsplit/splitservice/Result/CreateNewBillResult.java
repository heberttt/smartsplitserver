package com.smartsplit.splitservice.Result;

import lombok.Data;

@Data
public class CreateNewBillResult {
    private Boolean success = false;

    private int statusCode;

    private String errorMessage;
}
