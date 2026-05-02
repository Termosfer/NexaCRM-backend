package com.nexacrm.api.repository;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param; // @Param üçün lazımdır
import org.springframework.stereotype.Repository;

import com.nexacrm.api.entity.Lead;
@Repository
public interface LeadRepository extends JpaRepository<Lead, UUID> {
  
	List<Lead> findByOrganizationIdOrderByCreatedAtDesc(UUID organizationId);
    
    long countByOrganizationId(UUID organizationId);

    @Query("SELECT SUM(l.amount) FROM Lead l WHERE l.organization.id = :orgId")
    BigDecimal sumAmountByOrganizationId(@Param("orgId") UUID orgId);
    
    // Spring Data JPA bu metodu avtomatik yaradacaq (Entity-də 'createdAt' olduğu müddətcə)
    long countByOrganizationIdAndCreatedAtBetween(UUID orgId, LocalDateTime start, LocalDateTime end);

    // JPQL sorğusu - @Param istifadə etmək daha doğrudur
    @Query("SELECT SUM(l.amount) FROM Lead l WHERE l.organization.id = :orgId AND l.createdAt BETWEEN :start AND :end")
    BigDecimal sumAmountByOrganizationIdAndCreatedAtBetween(
            @Param("orgId") UUID orgId, 
            @Param("start") LocalDateTime start, 
            @Param("end") LocalDateTime end
    );
}