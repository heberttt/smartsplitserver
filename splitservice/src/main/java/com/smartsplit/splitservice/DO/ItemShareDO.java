package com.smartsplit.splitservice.DO;

import java.math.BigDecimal;

import lombok.Data;

@Data
public class ItemShareDO {
    private Integer id;
    private Integer billItemId;
    private Integer participantId;
    private BigDecimal quantityShare;

}