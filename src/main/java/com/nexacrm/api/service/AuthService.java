package com.nexacrm.api.service;

import com.nexacrm.api.dto.*;
import com.nexacrm.api.entity.*;
import com.nexacrm.api.repository.*;
import com.nexacrm.api.security.JwtUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final OrganizationRepository organizationRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtils jwtUtils;

    @Transactional
    public User registerAdmin(RegisterRequest dto) {
        // 1. Şirkəti yarat
        Organization org = Organization.builder()
                .nameString(dto.getCompanyName())
                .build();
        org = organizationRepository.save(org);

        // 2. Admini yarat
        User user = User.builder()
                .name(dto.getFullName())
                .email(dto.getEmail())
                .password(passwordEncoder.encode(dto.getPassword()))
                .role(Role.ADMIN)
                .organization(org)
                .build();

        return userRepository.save(user);
    }

    public AuthResponse login(LoginRequest dto) {
        User user = userRepository.findByEmail(dto.getEmail())
                .orElseThrow(() -> new RuntimeException("E-poçt və ya şifrə yanlışdır"));

        if (!passwordEncoder.matches(dto.getPassword(), user.getPassword())) {
            throw new RuntimeException("E-poçt və ya şifrə yanlışdır");
        }

        // Token yaradılır
        String token = jwtUtils.generateToken(
            user.getEmail(), 
            user.getOrganization().getId(), 
            user.getRole().name()
        );

        return new AuthResponse(
            token, 
            user.getEmail(), 
            user.getRole().name(), 
            user.getOrganization().getId(),
            user.getOrganization().getNameString()
        );
    }
}