package com.smartsplit.receiptparserservice.Processor;

import com.smartsplit.receiptparserservice.DTO.TransformDataDTO;
import com.smartsplit.receiptparserservice.Result.TransformDataResult;

public interface DataProcessorStrategy {
    public TransformDataResult processData(TransformDataDTO data);
}
