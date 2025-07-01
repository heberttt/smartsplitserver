package com.smartsplit.accountservice.Results;

import com.smartsplit.accountservice.Model.AccountDO;

import lombok.Data;

@Data
public class LoginResult {

    private AccountDO accountDetails;

    private Boolean success = false;

    private int statusCode;

    private String errorMessage;
}
