package com.smartsplit.authservice.Clients.Results;

import com.smartsplit.authservice.Models.AccountDO;

import lombok.Data;

@Data
public class GetAccountResult {
    private AccountDO accountDetails;

    private Boolean success = false;

    private int statusCode;

    private String errorMessage;
}


