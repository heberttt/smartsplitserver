package com.smartsplit.accountservice.Result;

import lombok.Data;

@Data
public class DeleteGroupResult {
    private Boolean success = false;

    private int statusCode;

    private String errorMessage;
}
