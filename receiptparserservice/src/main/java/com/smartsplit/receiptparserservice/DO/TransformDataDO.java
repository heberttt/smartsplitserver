package com.smartsplit.receiptparserservice.DO;

import java.util.List;

import lombok.Data;

@Data
public class TransformDataDO {
    private String title;
    private List<String> items;
    private List<Double> prices;
    private List<Integer> quantity;
    private Integer additionalChargesPercent;
    private Double roundingAdjustment;
}
