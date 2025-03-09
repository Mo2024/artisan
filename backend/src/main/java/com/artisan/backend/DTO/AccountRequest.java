package com.artisan.backend.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AccountRequest {
    private String addedBalance;
    private Integer accountId;
    private  String description;
    private  Date date;
}
