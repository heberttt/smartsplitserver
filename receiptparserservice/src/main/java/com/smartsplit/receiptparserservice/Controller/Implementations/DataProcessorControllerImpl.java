package com.smartsplit.receiptparserservice.Controller.Implementations;

import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.smartsplit.receiptparserservice.Controller.DataProcessorController;
import com.smartsplit.receiptparserservice.DTO.TransformDataDTO;
import com.smartsplit.receiptparserservice.Result.TransformDataResult;
import com.smartsplit.receiptparserservice.Service.DataProcessorService;


@RestController
@RequestMapping("/api/dataProcess")
public class DataProcessorControllerImpl implements DataProcessorController{

    private final DataProcessorService dataProcessorService;

    public DataProcessorControllerImpl(DataProcessorService dataProcessorService){
        this.dataProcessorService = dataProcessorService;
    }

    @PostMapping("/transform")
    public ResponseEntity<TransformDataResult> transformData(@RequestBody TransformDataDTO request) {
        TransformDataResult result = dataProcessorService.processData(request);
        
        return new ResponseEntity<>(result, HttpStatusCode.valueOf(result.getStatusCode()));
    }
    
}
