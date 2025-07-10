package com.smartsplit.accountservice.DO;

import java.util.List;

import lombok.Data;

@Data
public class GroupDO {
    private int id;
    private String name;

    private List<AccountDO> members;
}
