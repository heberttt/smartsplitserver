package com.smartsplit.accountservice.Result;

import com.smartsplit.accountservice.DO.GroupDO;

import lombok.Data;

@Data
public class InviteFriendResult {
    private Boolean success = false;

    private int statusCode;

    private String errorMessage;

    private GroupDO data;
}
