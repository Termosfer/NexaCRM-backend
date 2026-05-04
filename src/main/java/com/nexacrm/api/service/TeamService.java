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
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class TeamService {

    private final UserRepository userRepository;
    private final DepartmentRepository departmentRepository;
    private final OrganizationRepository organizationRepository;
    private final PasswordEncoder passwordEncoder;

    // ==========================================
    // 1. İŞÇİ (USER) İDARƏETMƏSİ
    // ==========================================

    @Transactional
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
                .bonusAmount(dto.getBonusAmount()) // Bonus əlavəsi
                .organization(org)
                .department(dept)
                .status(UserStatus.ACTIVE) // Yeni işçi aktiv olur
                .build();

        return userRepository.save(user);
    }

    // Şirkətin Bütün İşçilərini Gətir
    public List<User> getTeamMembers(UUID orgId) {
        // Gələcəkdə yalnız aktiv işçiləri gətirmək istəsəniz, Repository-də 
        // findByOrganizationIdAndStatus(orgId, UserStatus.ACTIVE) yaza bilərsiniz
        return userRepository.findByOrganizationId(orgId);
    }

    // YENİ: İŞÇİNİ YENİLƏ (Vəzifə artımı, Bonus, Şöbə transferi)
    @Transactional
    public User updateUser(UUID userId, CreateUserDTO dto) {
        User existingUser = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("İşçi tapılmadı"));

        existingUser.setName(dto.getName());
        existingUser.setEmail(dto.getEmail());

        // Ağıllı Şifrə: Əgər boş göndərilibsə, köhnə şifrəni dəyişmirik
        if (dto.getPassword() != null && !dto.getPassword().trim().isEmpty()) {
            existingUser.setPassword(passwordEncoder.encode(dto.getPassword()));
        }

        existingUser.setRole(dto.getRole());
        existingUser.setJobTitle(dto.getJobTitle());
        existingUser.setSalary(dto.getSalary());
        
        if (dto.getBonusAmount() != null) {
            existingUser.setBonusAmount(dto.getBonusAmount());
        }

        // Şöbə transferi
        if (dto.getDepartmentId() != null) {
            Department dept = departmentRepository.findById(dto.getDepartmentId())
                    .orElseThrow(() -> new RuntimeException("Şöbə tapılmadı"));
            existingUser.setDepartment(dept);
        } else {
            existingUser.setDepartment(null);
        }

        return userRepository.save(existingUser);
    }

    // YENİ: İŞÇİNİ SİL (Soft Delete məntiqi ilə)
    @Transactional
    public void deleteUser(UUID userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("İşçi tapılmadı"));
        
        // İşçini bazadan silmək təhlükəlidir, çünki onun keçmiş satışları ola bilər
        // Biz sadəcə onun statusunu "QOVULDU" edirik ki, sistemə girə bilməsin.
        user.setStatus(UserStatus.FIRED);
        userRepository.save(user);
        
        // DİQQƏT: Əgər mütləq fiziki silmək (bazadan yox etmək) istəyirsinizsə:
        // userRepository.deleteById(userId); 
        // (Amma bu zaman User-ə bağlı Leads varsa xəta verəcək)
    }

    // ==========================================
    // 2. ŞÖBƏ (DEPARTMENT) İDARƏETMƏSİ
    // ==========================================

    @Transactional
    public Department createDepartment(String name, UUID orgId) {
        Organization org = organizationRepository.findById(orgId).orElseThrow();
        Department dept = Department.builder()
                .name(name)
                .organization(org)
                .build();
        return departmentRepository.save(dept);
    }

    public List<Department> getDepartments(UUID orgId) {
        return departmentRepository.findByOrganizationId(orgId);
    }

    @Transactional
    public Department updateDepartment(UUID id, String newName) {
        Department dept = departmentRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Şöbə tapılmadı"));
        dept.setName(newName);
        return departmentRepository.save(dept);
    }

    // Şöbəni (Departamenti) Silmək
    @Transactional
    public void deleteDepartment(UUID id) {
        // Əgər şöbənin içində işçilər varsa, silinməsinə icazə verməmək üçün yoxlama:
        Department dept = departmentRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Şöbə tapılmadı"));
        
        if (dept.getUsers() != null && !dept.getUsers().isEmpty()) {
            throw new RuntimeException("Bu şöbədə işçilər var! Əvvəlcə onları silin və ya başqa şöbəyə köçürün.");
        }
        
        departmentRepository.deleteById(id);
    }

 // 1. SƏBƏBLİ SİLMƏ
    @Transactional
    public void deleteUser(UUID userId, UserStatus status) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("İşçi tapılmadı"));
        
        // Front-end-dən göndərilən statusu (FIRED və ya RESIGNED) tətbiq edirik
        user.setStatus(status != null ? status : UserStatus.FIRED);
        userRepository.save(user);
    }

    // 2. GERİ QAYTARMA (RESTORE)
    @Transactional
    public void restoreUser(UUID userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("İşçi tapılmadı"));
        
        user.setStatus(UserStatus.ACTIVE); // Yenidən işə bərpa edirik
        userRepository.save(user);
    }
}