package com.nexacrm.api.repository;

import com.nexacrm.api.entity.Customer;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Repository
public interface CustomerRepository extends JpaRepository<Customer, UUID> {

	List<Customer> findByOrganizationIdOrderByCreatedAtDesc(UUID organizationId);

	// BAX, BU METODU ƏLAVƏ ETMƏLİSƏN:
	long countByOrganizationIdAndCreatedAtBetween(UUID orgId, LocalDateTime start, LocalDateTime end);

	// Aktiv olanları və axtarış şərtlərini birləşdiririk
	@Query("SELECT c FROM Customer c WHERE c.organization.id = :orgId AND c.active = :status AND " +
		       "(LOWER(c.firstName) LIKE LOWER(CONCAT('%', :query, '%')) OR " +
		       "LOWER(c.lastName) LIKE LOWER(CONCAT('%', :query, '%')) OR " +
		       "LOWER(c.email) LIKE LOWER(CONCAT('%', :query, '%')) OR " +
		       "LOWER(c.companyName) LIKE LOWER(CONCAT('%', :query, '%')))") 
		Page<Customer> searchCustomers(
		    @Param("orgId") UUID orgId, 
		    @Param("query") String query, 
		    @Param("status") boolean status, 
		    Pageable pageable
		);

}