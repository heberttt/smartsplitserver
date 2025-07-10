package com.smartsplit.accountservice.Result;

import java.util.List;

import com.smartsplit.accountservice.DO.GroupDO;

import lombok.Data;

@Data
public class GetMyGroupsResult {
    private Boolean success = false;

    private int statusCode;

    private String errorMessage;

    private List<GroupDO> data;
}
