package com.nexacrm.api.repository;


import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.nexacrm.api.entity.User;

@Repository
public interface UserRepository extends JpaRepository<User, UUID>{

	List<User> findByOrganizationId(UUID organizationId);
    boolean existsByEmail(String email);
    Optional<User> findByEmail(String email); // BU SƏTİRİ ƏLAVƏ ET
}
