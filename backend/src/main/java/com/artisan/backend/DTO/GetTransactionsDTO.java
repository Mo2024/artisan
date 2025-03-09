package com.artisan.backend.DTO;

import com.artisan.backend.model.Account;
import com.artisan.backend.model.Deposit;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class GetTransactionsDTO {
    private List<Cash> transactions;
    private List<Deposit> deposits;
    private  Account account;
}
