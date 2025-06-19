package com.smartsplit.receiptparserservice.Service.Implementations;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import com.smartsplit.receiptparserservice.DTO.TransformDataDTO;
import com.smartsplit.receiptparserservice.Processor.DataProcessorStrategy;
import com.smartsplit.receiptparserservice.Result.TransformDataResult;
import com.smartsplit.receiptparserservice.Service.DataProcessorService;


@Service
public class DataProcessorServiceImpl implements DataProcessorService {

    private DataProcessorStrategy dataProcessor;

    public DataProcessorServiceImpl(@Qualifier("geminiDataProcessor") DataProcessorStrategy dataProcessor){
        this.dataProcessor = dataProcessor;
    }

    @Override
    public TransformDataResult processData(TransformDataDTO dto) {
        return dataProcessor.processData(dto);
    }
    
}
