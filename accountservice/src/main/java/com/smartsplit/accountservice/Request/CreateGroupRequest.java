package com.smartsplit.accountservice.Request;

import java.util.List;

import lombok.Data;

@Data
public class CreateGroupRequest {
    private String name;
    private List<String> otherMembersId;
}
