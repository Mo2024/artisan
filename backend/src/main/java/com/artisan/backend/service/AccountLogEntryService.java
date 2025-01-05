package com.artisan.backend.service;

import com.artisan.backend.model.AccountLogEntry;
import com.artisan.backend.repository.AccountLogEntryRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Date;

@Service
public class AccountLogEntryService {

    @Autowired
    private AccountLogEntryRepository aleRepository;

    @Transactional
    public void insertLog(
            Integer transactionId,
            Integer accountId,
            Integer userId,
            Integer siteId,
            BigDecimal balanceBefore,
            BigDecimal balanceAfter,
            BigDecimal cost,
            String template
    ){
        AccountLogEntry logEntry = new AccountLogEntry();

        logEntry.setTransactionId(transactionId);
        logEntry.setAccountId(accountId);
        logEntry.setUserId(userId);
        logEntry.setBalanceBefore(balanceBefore);
        logEntry.setBalanceAfter(balanceAfter);
        logEntry.setCost(cost);
        logEntry.setTemplate(template);
        logEntry.setDateExecuted(LocalDateTime.now());
        logEntry.setSiteId(siteId);
        aleRepository.save(logEntry);
    }
}
