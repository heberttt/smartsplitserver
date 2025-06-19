package com.smartsplit.receiptparserservice.Service;

import com.smartsplit.receiptparserservice.DTO.TransformDataDTO;
import com.smartsplit.receiptparserservice.Result.TransformDataResult;

public interface DataProcessorService {
    public TransformDataResult processData(TransformDataDTO dto);
}
