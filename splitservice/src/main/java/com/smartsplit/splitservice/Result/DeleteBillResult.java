package com.smartsplit.splitservice.Result;

import lombok.Data;

@Data
public class DeleteBillResult {
    private Boolean success = false;

    private int statusCode;

    private String errorMessage;
}
