package com.nexacrm.api.controller;

import java.util.UUID;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.nexacrm.api.entity.Customer;
import com.nexacrm.api.repository.CustomerRepository;
import com.nexacrm.api.service.CustomerService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/v1/customers")
@RequiredArgsConstructor // BU ANNOTASİYA BÜTÜN "FINAL" SAHƏLƏR ÜÇÜN CONSTRUCTOR-U ÖZÜ YARADIR
public class CustomerController {

    private final CustomerRepository customerRepository;
    private final CustomerService customerService;

    // --- MANUAL CONSTRUCTOR SİLİNDİ (Lombok artıq bunu edir) ---

 // CustomerController.java

    @PostMapping
    public ResponseEntity<Customer> createCustomer(@RequestBody com.nexacrm.api.dto.CustomerCreateDTO dto) {
        return ResponseEntity.ok(customerService.saveCustomer(dto));
    }

    @GetMapping("/org/{orgId}")
    public ResponseEntity<Page<Customer>> getCustomers(
            @PathVariable UUID orgId,
            @RequestParam(defaultValue = "") String query,
            @RequestParam(defaultValue = "true") boolean active,
            @RequestParam(defaultValue = "firstName") String sortBy, // Hansı sütuna görə?
            @RequestParam(defaultValue = "asc") String direction,    // A-Z yoxsa Z-A?
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        
        return ResponseEntity.ok(customerService.getCustomersPaged(orgId, query, active, sortBy, direction, page, size));
    }

    @PatchMapping("/{id}/restore")
    public ResponseEntity<Void> restoreCustomer(@PathVariable UUID id) {
        Customer c = customerRepository.findById(id).orElseThrow();
        c.setActive(true);
        customerRepository.save(c);
        return ResponseEntity.ok().build();
    }

    @PutMapping("/{id}")
    public ResponseEntity<Customer> updateCustomer(
            @PathVariable UUID id, 
            @RequestBody com.nexacrm.api.dto.CustomerUpdateDTO updateDto) { // Artıq DTO istifadə edirik
        
        return ResponseEntity.ok(customerService.updateCustomer(id, updateDto));
    }
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCustomer(@PathVariable UUID id) {
        customerService.deleteCustomer(id); // Burada Service daxilində soft delete işləməlidir
        return ResponseEntity.noContent().build();
    }
}