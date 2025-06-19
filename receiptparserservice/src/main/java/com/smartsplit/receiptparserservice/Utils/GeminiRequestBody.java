package com.smartsplit.receiptparserservice.Utils;

import java.util.List;
import java.util.Map;

import lombok.Getter;

@Getter
public class GeminiRequestBody {
    private Map<String, Object> contentMap;

    public GeminiRequestBody(String instruction, String ocrText){
        buildContentMap(instruction, ocrText);
    }

    private void buildContentMap(String instruction, String ocrText){
        Map<String, Object> instructionTextMap = Map.of("text", instruction);
        Map<String, Object> instructionPartMap = Map.of("parts", List.of(instructionTextMap), "role", "user");
        
        Map<String, Object> ocrTextMap = Map.of("text", ocrText);
        Map<String, Object> ocrPartMap = Map.of("parts", List.of(ocrTextMap), "role", "user");

        Map<String, Object> contentMap = Map.of("contents", List.of(instructionPartMap, ocrPartMap), "generation_config" , Map.of("response_mime_type", "application/json"));

        this.contentMap = contentMap;
    }

}


// Map<String, Object> textMap = Map.of("text", "What is an apple?");
// Map<String, Object> partMap = Map.of("parts", List.of(textMap));
// Map<String, Object> contentMap = Map.of("contents", List.of(partMap));
