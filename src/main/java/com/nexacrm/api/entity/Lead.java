package com.nexacrm.api.entity;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Map;
import java.util.UUID;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "leads")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Lead {

	@Id
	@GeneratedValue(strategy = GenerationType.UUID)
	private UUID id;

	@Column(nullable = false)
	private String title; // Məsələn: "10 ədəd laptop satışı"

	private String description;

	@Enumerated(EnumType.STRING)
	@Column(nullable = false)
	private LeadStatus status; // Kanban lövhəsindəki sütunu təyin edir

	private BigDecimal amount; // Satışın ehtimal olunan məbləği

	@ManyToOne(fetch = FetchType.EAGER) // Satışı gətirəndə müştəri məlumatını da gətirsin
	@JoinColumn(name = "customer_id", nullable = false) // Bazada sütun adı customer_id olacaq
	private Customer customer;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "organization_id", nullable = false)
	private Organization organization;

	@CreationTimestamp
	@Column(updatable = false)
	private LocalDateTime createdAt;
	
	// 1. Bu satış (müştəri) hansı işçiyə (maklerə) tapşırılıb?
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "assigned_user_id")
    private User assignedUser;

    // 2. SEHİRLİ SÜTUN: Hər sektora və müraciət növünə (İcarə/Satış) uyğun dinamik məlumatlar burada saxlanacaq
    @JdbcTypeCode(SqlTypes.JSON)
    @Column(columnDefinition = "jsonb")
    private Map<String, Object> customDetails;
	
}
