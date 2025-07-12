package com.smartsplit.splitservice.DO;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import lombok.Data;

@Data
public class BillDO {
    private Integer id;
    private String name;
    private String payerId;
    private BigDecimal extraCharges;
    private BigDecimal rounding;
    private LocalDateTime createdAt;
}