package com.smartsplit.splitservice.Request;

import lombok.Data;

import com.smartsplit.splitservice.Model.Receipt;

@Data
public class CreateNewBillRequest {
    private Receipt receipt;
    private String groupId;
}





