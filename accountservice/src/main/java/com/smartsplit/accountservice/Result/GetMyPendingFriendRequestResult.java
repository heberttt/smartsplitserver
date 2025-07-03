package com.smartsplit.accountservice.Result;

import java.util.Map;

import com.smartsplit.accountservice.DO.AccountDO;

import lombok.Data;

@Data
public class GetMyPendingFriendRequestResult {
    private Boolean success = false;

    private int statusCode;

    private String errorMessage;

    private Map<Integer, AccountDO> data;
}
