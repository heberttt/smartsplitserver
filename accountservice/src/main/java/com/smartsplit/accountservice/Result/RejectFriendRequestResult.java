package com.smartsplit.accountservice.Result;

import lombok.Data;

@Data
public class RejectFriendRequestResult{
    private Boolean success = false;

    private int statusCode;

    private String errorMessage;
}