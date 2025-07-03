package com.smartsplit.accountservice.Result;

import com.smartsplit.accountservice.DO.AccountDO;

import lombok.Data;

@Data
public class RemoveFriendResult {
    private Boolean success = false;

    private int statusCode;

    private String errorMessage;

    private AccountDO data;
}
