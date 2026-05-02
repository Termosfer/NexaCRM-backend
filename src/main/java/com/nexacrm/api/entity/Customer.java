package com.nexacrm.api.entity;

import java.time.LocalDateTime;
import java.util.UUID;

import org.hibernate.annotations.CreationTimestamp;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
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
@Table(name ="customers") // Verilənlər bazasında cədvəl adı
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@JsonIgnoreProperties(ignoreUnknown = true)
public class Customer {

	@Id
	@GeneratedValue(strategy = GenerationType.UUID)// ID-ni avtomatik UUID formatında yaradır
	private UUID id;
	
	@Column(nullable = false) // Müştərinin adı mütləq olmalıdır
	private String firstName;
	
	@Column(nullable = false)
	private String lastName;
	
	@Column(nullable = true)  // Eyni email ilə iki müştəri olmasın (opsional)
	private String email;
	
	
	private String phone;
	
	private String companyName; // Müştərinin çalışdığı şirkət (bizim müştərimiz olan şirkət yox)
	
	// Yeni sahəni əlavə et
	@Column(nullable = false)
	private boolean active = true; // Varsayılan olaraq aktivdir
	// MULTI-TENANT MƏNTİQİ:
    // Bu müştəri NexaCRM-dən istifadə edən hansı şirkətə məxsusdur?
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "organization_id",nullable = false)
	private Organization organization;
	
	
	@CreationTimestamp  // Müştəri bazaya əlavə olunanda vaxtı avtomatik qeyd edir
	@Column(updatable = false)
	private LocalDateTime createdAt;

}
