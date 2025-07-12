package com.smartsplit.splitservice.DO;

import java.math.BigDecimal;

import lombok.Data;

@Data
public class BillItemDO {
    private Integer id;
    private Integer billId;
    private String itemName;
    private Integer quantity;
    private BigDecimal price;
}
