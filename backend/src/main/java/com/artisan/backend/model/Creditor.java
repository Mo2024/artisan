package com.artisan.backend.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "creditors")
@Data
@NoArgsConstructor
public class Creditor {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "creditor_seq")
    @SequenceGenerator(name = "creditor_seq", sequenceName = "creditor_sequence", initialValue = 1, allocationSize = 1)
    private Integer id;

    @Column(name = "name")
    private String name;

    @Column(name = "description")
    private String description;

    @Column(name = "is_archived")
    private boolean isArchived;

    @OneToMany(mappedBy = "creditor", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Credit> credits = new ArrayList<>();

}
