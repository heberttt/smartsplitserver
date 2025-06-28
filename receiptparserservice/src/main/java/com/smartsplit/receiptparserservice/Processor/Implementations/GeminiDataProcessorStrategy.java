package com.smartsplit.receiptparserservice.Processor.Implementations;

import org.springframework.stereotype.Service;

import com.smartsplit.receiptparserservice.DTO.TransformDataDTO;
import com.smartsplit.receiptparserservice.Processor.DataProcessorStrategy;
import com.smartsplit.receiptparserservice.Result.TransformDataResult;
import com.smartsplit.receiptparserservice.Utils.GeminiUtil;
import com.smartsplit.receiptparserservice.Utils.TransformDataResultConverter;

@Service("geminiDataProcessor")
public class GeminiDataProcessorStrategy implements DataProcessorStrategy {

    private final GeminiUtil geminiUtil; 

    private final TransformDataResultConverter transformDataResultConverter;

    public GeminiDataProcessorStrategy(GeminiUtil geminiUtil, TransformDataResultConverter transformDataResultConverter){
        this.geminiUtil = geminiUtil;
        this.transformDataResultConverter = transformDataResultConverter;
    }

    @Override
    public TransformDataResult processData(TransformDataDTO data) {

        String apiResult = "";

        TransformDataResult result = new TransformDataResult();

        try{
            apiResult = geminiUtil.callApi(data);
            System.out.println(apiResult);
        }catch(Exception e){
            result.setSuccess(false);
            result.setErrorMessage(e.toString());
            result.setStatusCode(500);
            return result;
        }

        try{
            result.setData(transformDataResultConverter.convertFromJson(apiResult));
            result.setSuccess(true);
            result.setStatusCode(200);
        }catch (Exception e){
            result.setSuccess(false);
            result.setErrorMessage(e.toString());
            result.setStatusCode(500);
        }


        return result;
        
    }
    
}
