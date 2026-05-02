package com.nexacrm.api.service;

import com.nexacrm.api.entity.Organization;
import com.nexacrm.api.repository.OrganizationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class OrganizationService {

    private final OrganizationRepository organizationRepository;

    public Organization createOrganization(Organization organization) {
        return organizationRepository.save(organization);
    }

    public List<Organization> getAllOrganizations() {
        return organizationRepository.findAll();
    }
}