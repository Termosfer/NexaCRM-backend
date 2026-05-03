package com.nexacrm.api.entity;



import org.hibernate.annotations.CreationTimestamp;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "users")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false, unique = true)
    private String email;

    @Column(nullable = false)
    private String password;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Role role;

    // Multi-tenant: Bu işçi hansı şirkətə aiddir?
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "organization_id", nullable = false)
    private Organization organization;

    @CreationTimestamp
    @Column(updatable = false)
    private LocalDateTime createdAt;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "department_id")
    private Department department; // Hansı şöbədə işləyir?

    private String jobTitle; // Vəzifəsi (Məs: Senior Makler)

    @Enumerated(EnumType.STRING)
    private UserStatus status = UserStatus.ACTIVE; // Default olaraq işə girən hər kəs Aktivdir

    private BigDecimal salary; // Aylıq maaş
    
    private BigDecimal bonusAmount; // Topladığı bonuslar (Ayın işçisi üçün)
}