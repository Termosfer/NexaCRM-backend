package com.nexacrm.api.service;

import com.nexacrm.api.dto.PasswordChangeDTO;
import com.nexacrm.api.dto.ProfileUpdateDTO;
import com.nexacrm.api.entity.User;
import com.nexacrm.api.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    // 1. İstifadəçi məlumatlarını gətir
    public User getUserById(UUID id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("İstifadəçi tapılmadı"));
    }

    // 2. Profili yenilə (Ad və Email)
    @Transactional
    public User updateProfile(UUID userId, ProfileUpdateDTO dto) {
        User user = getUserById(userId);
        
        // Email dəyişibsə, bazada başqa birində olub-olmadığını yoxlaya bilərsən
        user.setName(dto.getName());
        user.setEmail(dto.getEmail());
        
        return userRepository.save(user);
    }

    // 3. Şifrəni dəyiş (Təhlükəsizlik yoxlaması ilə)
    @Transactional
    public void changePassword(UUID userId, PasswordChangeDTO dto) {
        User user = getUserById(userId);

        // Köhnə şifrənin doğruluğunu yoxlayırıq
        if (!passwordEncoder.matches(dto.getOldPassword(), user.getPassword())) {
            throw new RuntimeException("Hazırkı şifrə yanlışdır!");
        }

        // Yeni şifrəni şifrələyib yadda saxlayırıq
        user.setPassword(passwordEncoder.encode(dto.getNewPassword()));
        userRepository.save(user);
    }
}