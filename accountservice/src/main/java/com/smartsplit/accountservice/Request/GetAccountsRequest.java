package com.smartsplit.accountservice.Request;

import java.util.List;

import lombok.Data;

@Data
public class GetAccountsRequest {
    private List<String> accountIds;
}
