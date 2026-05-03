package com.nexacrm.api.service;

import com.nexacrm.api.dto.CreateUserDTO;
import com.nexacrm.api.entity.Department;
import com.nexacrm.api.entity.Organization;
import com.nexacrm.api.entity.User;
import com.nexacrm.api.entity.UserStatus;
import com.nexacrm.api.repository.DepartmentRepository;
import com.nexacrm.api.repository.OrganizationRepository;
import com.nexacrm.api.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class TeamService {

    private final UserRepository userRepository;
    private final DepartmentRepository departmentRepository;
    private final OrganizationRepository organizationRepository;
    private final PasswordEncoder passwordEncoder;

    // 1. Yeni İşçi Əlavə Et (Admin və ya Manager tərəfindən)
    public User createUser(CreateUserDTO dto) {
        Organization org = organizationRepository.findById(dto.getOrganizationId())
                .orElseThrow(() -> new RuntimeException("Şirkət tapılmadı"));

        Department dept = null;
        if (dto.getDepartmentId() != null) {
            dept = departmentRepository.findById(dto.getDepartmentId()).orElse(null);
        }

        User user = User.builder()
                .name(dto.getName())
                .email(dto.getEmail())
                .password(passwordEncoder.encode(dto.getPassword()))
                .role(dto.getRole())
                .jobTitle(dto.getJobTitle())
                .salary(dto.getSalary())
                .organization(org)
                .department(dept)
                .status(UserStatus.ACTIVE)
                .build();

        return userRepository.save(user);
    }

    // 2. Şirkətin Bütün İşçilərini Gətir
    public List<User> getTeamMembers(UUID orgId) {
        return userRepository.findByOrganizationId(orgId);
    }

    // 3. Şöbə (Departament) Yaratmaq
    public Department createDepartment(String name, UUID orgId) {
        Organization org = organizationRepository.findById(orgId).orElseThrow();
        Department dept = Department.builder()
                .name(name)
                .organization(org)
                .build();
        return departmentRepository.save(dept);
    }

    // 4. Şirkətin Şöbələrini Gətir
    public List<Department> getDepartments(UUID orgId) {
        return departmentRepository.findByOrganizationId(orgId);
    }
}