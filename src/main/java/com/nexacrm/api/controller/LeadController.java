package com.nexacrm.api.controller;

import java.util.List;
import java.util.UUID;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.nexacrm.api.entity.Lead;
import com.nexacrm.api.entity.LeadStatus;
import com.nexacrm.api.service.LeadService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/v1/leads")
@RequiredArgsConstructor
public class LeadController {

	
	private final LeadService leadService;
	
	@PostMapping
	public ResponseEntity<Lead> createLead(@RequestBody com.nexacrm.api.dto.LeadCreateDTO dto) {
	    return ResponseEntity.ok(leadService.createLead(dto));
	}
	
	@GetMapping("/org/{orgId}")
    public ResponseEntity<List<Lead>> getLeadsByOrg(@PathVariable UUID orgId) {
        return ResponseEntity.ok(leadService.getLeadsByOrg(orgId));
    }

    // Satışın statusunu dəyişmək (Məs: NEW -> CONTACTED)
    @PatchMapping("/{id}/status")
    public ResponseEntity<Lead> updateStatus(
            @PathVariable UUID id, 
            @RequestParam LeadStatus status) {
        return ResponseEntity.ok(leadService.updateStatus(id, status));
    }
	
	
	
}
