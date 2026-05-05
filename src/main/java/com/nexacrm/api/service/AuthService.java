package com.nexacrm.api.service;

import com.nexacrm.api.dto.*;
import com.nexacrm.api.entity.*;
import com.nexacrm.api.repository.*;
import com.nexacrm.api.security.JwtUtils;
import lombok.RequiredArgsConstructor;

import java.time.LocalDateTime;
import java.util.UUID;
import java.util.Optional;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final OrganizationRepository organizationRepository;
    private final PasswordResetTokenRepository tokenRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtils jwtUtils;

    @Transactional
    public User registerAdmin(RegisterRequest dto) {
        Organization org = Organization.builder()
                .nameString(dto.getCompanyName())
                .businessSector(dto.getBusinessSector())
                .build();
        org = organizationRepository.save(org);

        User user = User.builder()
                .name(dto.getFullName())
                .email(dto.getEmail())
                .password(passwordEncoder.encode(dto.getPassword()))
                .role(Role.ADMIN)
                .organization(org)
                .status(UserStatus.ACTIVE) // <--- BAX BUNU ƏLAVƏ ETMƏLİSƏN
                .build();

        return userRepository.save(user);
    }

  public AuthResponse login(LoginRequest dto) {
        User user = userRepository.findByEmail(dto.getEmail())
                .orElseThrow(() -> new RuntimeException("E-poçt və ya şifrə yanlışdır"));

        if (!passwordEncoder.matches(dto.getPassword(), user.getPassword())) {
            throw new RuntimeException("E-poçt və ya şifrə yanlışdır");
        }

        String token = jwtUtils.generateToken(
            user.getEmail(), 
            user.getOrganization().getId(), 
            user.getRole().name()
        );

        return new AuthResponse(
            token, 
            user.getId(),          // YENİ ƏLAVƏ (İstifadəçi ID-si)
            user.getEmail(), 
            user.getName(),        // YENİ ƏLAVƏ (İstifadəçi Adı)
            user.getRole().name(), 
            user.getOrganization().getId(),
            user.getOrganization().getNameString(),
            user.getOrganization().getBusinessSector() // YENİ ƏLAVƏ
        );
    }

    @Transactional
    public void forgotPassword(String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Bu email-lə istifadəçi tapılmadı"));

        tokenRepository.deleteByUser(user);

        String token = UUID.randomUUID().toString();
        PasswordResetToken resetToken = new PasswordResetToken(token, user);
        tokenRepository.save(resetToken);

        System.out.println("ŞİFRƏ SIFIRLAMA LİNKİ: http://localhost:5173/reset-password?token=" + token);
    }

    @Transactional
    public void resetPassword(String token, String newPassword) {
        PasswordResetToken resetToken = tokenRepository.findByToken(token)
                .orElseThrow(() -> new RuntimeException("Keçərsiz və ya istifadə olunmuş token"));

        if (resetToken.getExpiryDate().isBefore(LocalDateTime.now())) {
            tokenRepository.delete(resetToken);
            throw new RuntimeException("Tokenin vaxtı bitib, yenidən müraciət edin");
        }

        User user = resetToken.getUser();
        user.setPassword(passwordEncoder.encode(newPassword));
        userRepository.save(user);
        tokenRepository.delete(resetToken);
    }
    
    // DÜZƏLDİLMİŞ METOD: "Cannot infer type" xətası burada həll olundu
    public String getSectorByEmail(String email) {
        // 1. userRepository (kiçik hərflə) istifadə olunmalıdır
        // 2. Metod referansları (::) tip xətalarının qarşısını alır
        return userRepository.findByEmail(email)
                .map(User::getOrganization)
                .map(Organization::getBusinessSector)
                .orElse("DEFAULT");
    }
}