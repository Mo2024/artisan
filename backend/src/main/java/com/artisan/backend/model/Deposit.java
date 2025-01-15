package com.artisan.backend.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.Date;

@Entity
@Table(name = "deposits")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Deposit {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "deposit_seq")
    @SequenceGenerator(name = "deposit_seq", sequenceName = "deposit_sequence", initialValue = 1, allocationSize = 1)
    private Integer id;

    @Column(name = "date")
    private Date date;

    @Column(name = "date_recorded")
    private Date dateRecorded;

    @Column(name = "cost")
    private BigDecimal cost;

    @Column(name = "description")
    private String description;

    @ManyToOne
    @JoinColumn(name = "user_id") // The foreign key column in the sites table
    @JsonIgnore
    private User user;

    @ManyToOne
    @JoinColumn(name = "account_id")
    @JsonIgnoreProperties("deposit")
    private Account account;
}
