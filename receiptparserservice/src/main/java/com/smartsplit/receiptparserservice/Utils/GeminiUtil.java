package com.smartsplit.receiptparserservice.Utils;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpRequest.BodyPublishers;
import java.net.http.HttpResponse;
import java.net.http.HttpResponse.BodyHandlers;
import java.util.List;

import org.springframework.stereotype.Component;

import com.google.gson.Gson;
import com.smartsplit.receiptparserservice.DTO.TransformDataDTO;

@Component
public class GeminiUtil {
    private String address = "https://generativelanguage.googleapis.com/v1beta/models/gemini-2.0-flash:generateContent?key=" + System.getenv("GOOGLE_API_KEY");
    private String instruction = "You are a receipt assistant. Return data as structured JSON with [title: string, items: string[], prices(before additional charges): double[], quantity(per item: int[]), extra_charges (percent): double, rounding_adj: double, total_price : double]. do not include words in '()' in json keys. Fix possible item name errors if confident";

    public String callApi(TransformDataDTO dto) throws IOException, InterruptedException, URISyntaxException{

        GeminiRequestBody body = new GeminiRequestBody(instruction, listToString(dto.getRec_texts()));

        Gson gson = new Gson();
        String json = gson.toJson(body.getContentMap());
        
        System.out.println(body.getContentMap());
        
        System.out.println(json);

        HttpRequest postRequest = HttpRequest.newBuilder()
            .uri(new URI(address))
            .header("Content-Type", "application/json")
            .POST(BodyPublishers.ofString(json))
            .build();

        HttpClient client = HttpClient.newHttpClient();

        HttpResponse<String> postResponse = client.send(postRequest, BodyHandlers.ofString());
        
        
        return postResponse.body();
    }


    private String listToString(List<String> list){
        System.out.println(list.toString());
        StringBuilder stringBuilder = new StringBuilder();
        for (int i = 0; i < list.size(); i++){
            if (i == list.size() - 1){
                stringBuilder.append("'" + list.get(i) + "'");
            }
            stringBuilder.append("'" + list.get(i) + "',");
        }
        
        return stringBuilder.toString();
    }


}
