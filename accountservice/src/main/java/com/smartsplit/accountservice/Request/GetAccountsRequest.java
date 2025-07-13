package com.smartsplit.accountservice.Request;

import java.util.List;

import lombok.Data;

@Data
public class GetAccountsRequest {
    List<String> accountIds;
}
