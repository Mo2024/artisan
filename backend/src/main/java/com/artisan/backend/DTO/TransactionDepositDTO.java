package com.artisan.backend.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TransactionDepositDTO {
    private Integer id;
    private Double cost;
    private Date date;
    private String type;
    // getters and setters
}
