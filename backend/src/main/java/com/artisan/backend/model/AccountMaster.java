package com.artisan.backend.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "account_master")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AccountMaster {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "account_master_seq")
    @SequenceGenerator(name = "account_master_seq", sequenceName = "account_master_sequence", initialValue = 1, allocationSize = 1)
    private Integer id;

    @Column(name = "name")
    private String name;

//    @OneToMany(mappedBy = "account_master", cascade = CascadeType.ALL, orphanRemoval = true)
//    @JsonIgnore
//    private List<JournalVoucher> cash = new ArrayList<>();

}
