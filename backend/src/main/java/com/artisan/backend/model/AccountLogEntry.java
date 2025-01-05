package com.artisan.backend.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Date;

@Entity
@Table(name = "accounts_log_entry")
@Data
@NoArgsConstructor
public class AccountLogEntry {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "ale_seq")
    @SequenceGenerator(name = "ale_seq", sequenceName = "ale_sequence", initialValue = 1, allocationSize = 1)
    private Long id;

    @Column(name = "transaction_id")
    private Integer transactionId;

    @Column(name = "account_id")
    private Integer accountId;

    @Column(name = "user_id")
    private Integer userId;

    @Column(name = "date_executed")
    private LocalDateTime dateExecuted;

    @Column(name = "balance_before")
    private BigDecimal balanceBefore;

    @Column(name = "balance_after")
    private BigDecimal balanceAfter;

    @Column(name = "cost")
    private BigDecimal cost;

    @Column(name = "template")
    private String template;

    @Column(name = "site_id")
    private Integer siteId;
}
