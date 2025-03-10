package com.artisan.backend.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.Date;

@Entity
@Table(name = "journal_voucher")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class JournalVoucher {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "jv_seq")
    @SequenceGenerator(name = "jv_seq", sequenceName = "jv_sequence", initialValue = 1, allocationSize = 1)
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "cr_master_id")
    @JsonIgnoreProperties("journal_voucher")
    private AccountMaster crMaster;

    @ManyToOne
    @JoinColumn(name = "dr_master_id")
    @JsonIgnoreProperties("journal_voucher")
    private AccountMaster drMaster;

    @ManyToOne
    @JoinColumn(name = "site_id")
    @JsonIgnoreProperties("journal_voucher")
    private Site site;

    @Column(name = "amount")
    private BigDecimal amount;

    @ManyToOne
    @JoinColumn(name = "user_id")
    @JsonIgnore
    private User user;

    @Column(name = "date")
    private Date date;

    @Column(name = "description")
    private String description;

    @Column(name = "date_recorded")
    private Date dateRecorded;

    @Column(name = "date_edited")
    private Date dateEdited;
}
