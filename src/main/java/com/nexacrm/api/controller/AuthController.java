package com.nexacrm.api.controller;

import com.nexacrm.api.dto.*;
import com.nexacrm.api.service.AuthService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
@CrossOrigin(origins = "http://localhost:5173")
public class AuthController {

    private final AuthService authService;

    @PostMapping("/register")
    public ResponseEntity<?> register(@Valid @RequestBody RegisterRequest request) {
        return ResponseEntity.ok(authService.registerAdmin(request));
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@Valid @RequestBody LoginRequest request) { // @Valid əlavə olundu
        return ResponseEntity.ok(authService.login(request));
    }
    @PostMapping("/forgot-password")
    public ResponseEntity<?> forgotPassword(@RequestParam String email) {
        authService.forgotPassword(email);
        return ResponseEntity.ok("Şifrə sıfırlama linki emailinizə göndərildi");
    }

    @PostMapping("/reset-password")
    public ResponseEntity<?> resetPassword(@RequestBody ResetPasswordDTO dto) {
        authService.resetPassword(dto.getToken(), dto.getNewPassword());
        return ResponseEntity.ok("Şifrə uğurla yeniləndi");
    }
 // YENİ METOD: Email yazılan kimi sektoru qaytarır
    @GetMapping("/sector-info")
    public ResponseEntity<java.util.Map<String, String>> getSectorInfo(@RequestParam String email) {
        // AuthService-dəki düzəltdiyimiz metodu çağırırıq
        String sector = authService.getSectorByEmail(email);
        return ResponseEntity.ok(java.util.Map.of("sector", sector));
    }
}