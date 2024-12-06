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
@Table(name = "credits")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Credit {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "credit_seq")
    @SequenceGenerator(name = "credit_seq", sequenceName = "credit_sequence", initialValue = 1, allocationSize = 1)
    private Integer id;

    @Column(name = "date")
    private Date date;

    @Column(name = "invoice_no")
    private String invoiceNo;

    @Column(name = "cost")
    private BigDecimal cost;

    @Column(name = "description")
    private String description;

    @Column(name = "date_recorded")
    private Date dateRecorded;

    @Column(name = "date_edited")
    private Date dateEdited;

    @Column(name = "is_paid")
    private boolean isPaid;

    @ManyToOne
    @JoinColumn(name = "site_id")
    @JsonIgnoreProperties({"cash", "credits"})
    private Site site;

    @ManyToOne
    @JoinColumn(name = "creditor_id")
    @JsonIgnoreProperties("credits")
    private Creditor creditor;

    @ManyToOne
    @JoinColumn(name = "user_id")
    @JsonIgnore
    private User user;



}
