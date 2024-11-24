package com.artisan.backend.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CashRequest {
    private Date date;
    private String paidBy;
    private String paymentMethod;
    private BigDecimal cost;
    private String description;
    private Integer accountId;
    private Integer siteId;
}
