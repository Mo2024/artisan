package com.artisan.backend.DTO;

import com.artisan.backend.model.Credit;
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
    private Integer id;
    private Boolean isCredit;
    private Integer creditId;
    private Credit credit;
    private String type;
}
