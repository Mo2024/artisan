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
public class AccountRequest {
    private String addedBalance;
    private Integer accountId;
}
