package com.artisan.backend.model;


import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.Date;

@Entity
@Table(name = "cash")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Cash {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "cash_seq")
    @SequenceGenerator(name = "cash_seq", sequenceName = "cash_sequence", initialValue = 1, allocationSize = 1)
    private Integer id;

    @Column(name = "date")
    private Date date;

    @Column(name = "paid_by")
    private String paidBy;

    @Column(name = "payment_method")
    private String paymentMethod;

    @Column(name = "cost")
    private BigDecimal cost;

    @Column(name = "description")
    private String description;

    @Column(name = "date_recorded")
    private Date dateRecorded;

    @Column(name = "date_edited")
    private Date dateEdited;

    @ManyToOne
    @JoinColumn(name = "site_id")
    @JsonIgnore
    private Site site;

    @ManyToOne
    @JoinColumn(name = "account_id")
    @JsonIgnore
    private Account account;

    @ManyToOne
    @JoinColumn(name = "user_id")
    @JsonIgnore
    private User user;



}
