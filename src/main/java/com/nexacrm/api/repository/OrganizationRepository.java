package com.nexacrm.api.repository;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.nexacrm.api.entity.Organization;

@Repository
public interface OrganizationRepository  extends JpaRepository<Organization, UUID> {

}
