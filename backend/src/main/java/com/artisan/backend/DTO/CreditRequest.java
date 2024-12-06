package com.artisan.backend.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CreditRequest {
    private Date date;
    private String invoiceNo;
    private BigDecimal cost;
    private String description;
    private Integer siteId;
    private Integer id;
    private Boolean isPaid;
    private  Integer creditorId;
}
