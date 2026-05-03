package com.nexacrm.api.entity;

import jakarta.persistence.*;
import lombok.*;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "departments")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class Department {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(nullable = false)
    private String name; // Məs: "Satış Şöbəsi", "Nərimanov Filialı"

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "organization_id", nullable = false)
    private Organization organization;

    // Bu şöbədə işləyən işçilərin siyahısı
    @OneToMany(mappedBy = "department", fetch = FetchType.LAZY)
    private List<User> users;
}