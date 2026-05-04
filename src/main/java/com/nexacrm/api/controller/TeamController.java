package com.nexacrm.api.controller;

import com.nexacrm.api.dto.CreateUserDTO;
import com.nexacrm.api.entity.Department;
import com.nexacrm.api.entity.User;
import com.nexacrm.api.entity.UserStatus;
import com.nexacrm.api.service.TeamService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/team")
@RequiredArgsConstructor
@CrossOrigin(origins = "http://localhost:5173")
public class TeamController {

    private final TeamService teamService;

    @PostMapping("/users")
    public ResponseEntity<User> createUser(@RequestBody CreateUserDTO dto) {
        return ResponseEntity.ok(teamService.createUser(dto));
    }

    @GetMapping("/users/{orgId}")
    public ResponseEntity<List<User>> getTeamMembers(@PathVariable UUID orgId) {
        return ResponseEntity.ok(teamService.getTeamMembers(orgId));
    }
    
    @PutMapping("/users/{id}")
    public ResponseEntity<User> updateUser(@PathVariable UUID id, @RequestBody CreateUserDTO dto) {
        return ResponseEntity.ok(teamService.updateUser(id, dto));
    }

    @DeleteMapping("/users/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable UUID id, @RequestParam(defaultValue = "FIRED") UserStatus status) {
        teamService.deleteUser(id, status);
        return ResponseEntity.noContent().build();
    }

    // Geri qaytarma endpoint-i
    @PatchMapping("/users/{id}/restore")
    public ResponseEntity<Void> restoreUser(@PathVariable UUID id) {
        teamService.restoreUser(id);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/departments")
    public ResponseEntity<Department> createDepartment(@RequestParam String name, @RequestParam UUID orgId) {
        return ResponseEntity.ok(teamService.createDepartment(name, orgId));
    }

    @GetMapping("/departments/{orgId}")
    public ResponseEntity<List<Department>> getDepartments(@PathVariable UUID orgId) {
        return ResponseEntity.ok(teamService.getDepartments(orgId));
    }
    @PutMapping("/departments/{id}")
    public ResponseEntity<Department> updateDepartment(@PathVariable UUID id, @RequestParam String name) {
        return ResponseEntity.ok(teamService.updateDepartment(id, name));
    }

    @DeleteMapping("/departments/{id}")
    public ResponseEntity<Void> deleteDepartment(@PathVariable UUID id) {
        teamService.deleteDepartment(id);
        return ResponseEntity.noContent().build();
    }
}