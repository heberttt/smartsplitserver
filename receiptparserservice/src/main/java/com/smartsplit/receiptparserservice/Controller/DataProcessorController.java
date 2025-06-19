package com.smartsplit.receiptparserservice.Controller;

import org.springframework.http.ResponseEntity;

import com.smartsplit.receiptparserservice.DTO.TransformDataDTO;
import com.smartsplit.receiptparserservice.Result.TransformDataResult;

public interface DataProcessorController {
    public ResponseEntity<TransformDataResult> transformData(TransformDataDTO request);
}
