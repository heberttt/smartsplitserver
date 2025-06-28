package com.smartsplit.receiptparserservice.Result;

import com.smartsplit.receiptparserservice.DO.TransformDataDO;

import lombok.Data;

@Data
public class TransformDataResult {

    private Boolean success = false;

    private int statusCode;

    private String errorMessage;

    private TransformDataDO data;

}