package com.smartsplit.receiptparserservice.DO;

import java.util.List;

import lombok.Data;

@Data
public class TransformDataDO {
    private String title;
    private List<String> items;
    private List<Double> prices;
    private Double additionalChargesPercent;
    private Double total;
    private Double roundingAdjustment;
}
