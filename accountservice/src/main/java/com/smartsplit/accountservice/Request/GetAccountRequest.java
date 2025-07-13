package com.smartsplit.accountservice.Request;

import java.util.List;

import lombok.Data;

@Data
public class GetAccountRequest {
    private List<String> account_id;
}
