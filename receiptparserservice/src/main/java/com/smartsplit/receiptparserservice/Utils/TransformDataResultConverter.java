package com.smartsplit.receiptparserservice.Utils;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Component;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.smartsplit.receiptparserservice.DO.TransformDataDO;

@Component
public class TransformDataResultConverter {
    public TransformDataDO convertFromJson(String json){
        
        Gson gson = new Gson();
        Gson gson1 = new Gson();

        JsonObject ocrResult = gson.fromJson(json, JsonObject.class);

        String ocrText = ocrResult.get("candidates").getAsJsonArray().get(0).getAsJsonObject().get("content")
            .getAsJsonObject().get("parts").getAsJsonArray().get(0).getAsJsonObject().get("text").getAsString();

        JsonObject ocrJson = gson1.fromJson(ocrText, JsonObject.class);
        
        List<String> items = new ArrayList<>();
        
        if (ocrJson.get("items").isJsonArray()){
            JsonArray array = ocrJson.get("items").getAsJsonArray();
            for (JsonElement e : array){
                items.add(e.getAsString());
            }
        }

        List<Double> prices = new ArrayList<>();
        
        if (ocrJson.get("prices").isJsonArray()){
            JsonArray array = ocrJson.get("prices").getAsJsonArray();
            for (JsonElement e : array){
                prices.add(e.getAsDouble());
            }
        }

        List<Integer> quantity = new ArrayList<>();

        if (ocrJson.get("quantity").isJsonArray()){
            JsonArray array = ocrJson.get("quantity").getAsJsonArray();
            for (JsonElement e : array){
                quantity.add(e.getAsInt());
            }
        }



        TransformDataDO result = new TransformDataDO();
        result.setTitle(ocrJson.get("title").toString());
        result.setItems(items);
        result.setPrices(prices);
        result.setQuantity(quantity);
        result.setAdditionalChargesPercent(ocrJson.get("extra_charges").getAsInt());
        result.setRoundingAdjustment(ocrJson.get("rounding_adj").getAsDouble());
        

        return result;
    }
}
