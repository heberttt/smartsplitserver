package com.smartsplit.accountservice.Result;

import java.util.List;

import com.smartsplit.accountservice.DO.AccountDO;

import lombok.Data;

@Data
public class GetAccountsResult {
    private Boolean success = false;

    private int statusCode;

    private String errorMessage;

    private List<AccountDO> data;
}
