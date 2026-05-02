package com.nexacrm.api.service;

import java.util.List;
import java.util.UUID;
import org.springframework.stereotype.Service;

import com.nexacrm.api.entity.Lead;
import com.nexacrm.api.entity.LeadStatus;
import com.nexacrm.api.entity.Customer;
import com.nexacrm.api.entity.Organization;
import com.nexacrm.api.dto.LeadCreateDTO;
import com.nexacrm.api.repository.LeadRepository;
import com.nexacrm.api.repository.CustomerRepository;
import com.nexacrm.api.repository.OrganizationRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class LeadService {

    private final LeadRepository leadRepository;
    private final CustomerRepository customerRepository; // Əlavə olundu
    private final OrganizationRepository organizationRepository; // Əlavə olundu

    // YENİLƏNMİŞ METOD: DTO qəbul edir
    public Lead createLead(LeadCreateDTO dto) {
        // 1. Müştərini tapırıq
        Customer customer = customerRepository.findById(dto.getCustomerId())
                .orElseThrow(() -> new RuntimeException("Müştəri tapılmadı"));

        // 2. Şirkəti tapırıq
        Organization org = organizationRepository.findById(dto.getOrganizationId())
                .orElseThrow(() -> new RuntimeException("Şirkət tapılmadı"));

        // 3. Lead obyektini Builder ilə (və ya set-lərlə) qururuq
        Lead lead = Lead.builder()
                .title(dto.getTitle())
                .description(dto.getDescription())
                .amount(dto.getAmount())
                .status(dto.getStatus() != null ? dto.getStatus() : LeadStatus.NEW)
                .customer(customer)
                .organization(org)
                .build();

        return leadRepository.save(lead);
    }

    public List<Lead> getLeadsByOrg(UUID orgId) {
    	return leadRepository.findByOrganizationIdOrderByCreatedAtDesc(orgId);
    }

    public Lead updateStatus(UUID leadId, LeadStatus newStatus) {
        Lead lead = leadRepository.findById(leadId)
                .orElseThrow(() -> new RuntimeException("Lead tapılmadı"));
        lead.setStatus(newStatus);
        return leadRepository.save(lead);
    }
}