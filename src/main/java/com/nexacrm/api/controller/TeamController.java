package com.nexacrm.api.controller;

import com.nexacrm.api.dto.CreateUserDTO;
import com.nexacrm.api.entity.Department;
import com.nexacrm.api.entity.User;
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

    @PostMapping("/departments")
    public ResponseEntity<Department> createDepartment(@RequestParam String name, @RequestParam UUID orgId) {
        return ResponseEntity.ok(teamService.createDepartment(name, orgId));
    }

    @GetMapping("/departments/{orgId}")
    public ResponseEntity<List<Department>> getDepartments(@PathVariable UUID orgId) {
        return ResponseEntity.ok(teamService.getDepartments(orgId));
    }
}