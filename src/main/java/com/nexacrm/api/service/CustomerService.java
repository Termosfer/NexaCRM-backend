package com.nexacrm.api.service;

import com.nexacrm.api.entity.Customer;
import com.nexacrm.api.entity.Organization;
import com.nexacrm.api.repository.CustomerRepository;
import com.nexacrm.api.repository.OrganizationRepository;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class CustomerService {

	private final CustomerRepository customerRepository;

	private final OrganizationRepository organizationRepository; 
	// 1. Yeni müştəri yaratmaq
	// CustomerService.java

	public Customer saveCustomer(com.nexacrm.api.dto.CustomerCreateDTO dto) {
	    // 1. Şirkəti tapırıq
	    Organization org = organizationRepository.findById(dto.getOrganizationId())
	            .orElseThrow(() -> new RuntimeException("Şirkət tapılmadı"));

	    // 2. DTO-dan Entity-yə çeviririk
	    Customer customer = Customer.builder()
	            .firstName(dto.getFirstName())
	            .lastName(dto.getLastName())
	            .email(dto.getEmail())
	            .phone(dto.getPhone())
	            .companyName(dto.getCompanyName())
	            .organization(org) // Əlaqəni qururuq
	            .active(true)      // Default olaraq aktiv
	            .build();

	    return customerRepository.save(customer);
	}

	// 2. Professional Səhifələmə və Axtarış
	public Page<Customer> getCustomersPaged(UUID orgId, String query, boolean active, String sortBy, String direction, int page, int size) {
	    // direction "asc" və ya "desc" olacaq
	    Sort sort = direction.equalsIgnoreCase("asc") 
	                ? Sort.by(sortBy).ascending() 
	                : Sort.by(sortBy).descending();
	                
	    Pageable pageable = PageRequest.of(page, size, sort);
	    return customerRepository.searchCustomers(orgId, query, active, pageable);
	}

	// 3. Müştəri məlumatlarını yeniləmək (Edit)
	public Customer updateCustomer(UUID id, com.nexacrm.api.dto.CustomerUpdateDTO dto) {
	    // 1. Bazadan köhnə müştərini tapırıq
	    Customer existing = customerRepository.findById(id)
	            .orElseThrow(() -> new RuntimeException("Müştəri tapılmadı"));

	    // 2. Yalnız DTO-dan gələn məlumatları köhnə obyektin üstünə yazırıq
	    existing.setFirstName(dto.getFirstName());
	    existing.setLastName(dto.getLastName());
	    existing.setEmail(dto.getEmail());
	    existing.setPhone(dto.getPhone());
	    existing.setCompanyName(dto.getCompanyName());

	    // 3. Organization sahəsinə toxunmuruq (çünki bazada o artıq var)
	    return customerRepository.save(existing);
	}

	// 4. Müştərini silmək (Delete)
	public void deleteCustomer(UUID id) {
		Customer customer = customerRepository.findById(id)
				.orElseThrow(() -> new RuntimeException("Müştəri tapılmadı"));

		// Fiziki silmirik, sadəcə statusunu dəyişirik
		customer.setActive(false);
		customerRepository.save(customer);
	}

	// 5. Müştərini ID-yə görə tapmaq
	public Customer getCustomerById(UUID id) {
		return customerRepository.findById(id).orElse(null);
	}
}