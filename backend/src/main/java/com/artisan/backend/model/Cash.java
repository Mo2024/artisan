package com.artisan.backend.model;


import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
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

    @Column(name = "cost")
    private BigDecimal cost;

    @Column(name = "description")
    private String description;

    @Column(name = "date_recorded")
    private Date dateRecorded;

    @Column(name = "date_edited")
    private Date dateEdited;

    @Column(name = "type")
    private String type;

    @Column(name = "isCredit")
    private Boolean isCredit;

    @ManyToOne
    @JoinColumn(name = "site_id")
    @JsonIgnoreProperties("cash")
    private Site site;

    @OneToOne
    @JoinColumn(name = "credit_id")
    private Credit credit;

    @ManyToOne
    @JoinColumn(name = "account_id")
    @JsonIgnoreProperties("cash")
    private Account account;

    @ManyToOne
    @JoinColumn(name = "user_id")
    @JsonIgnore
    private User user;



}
