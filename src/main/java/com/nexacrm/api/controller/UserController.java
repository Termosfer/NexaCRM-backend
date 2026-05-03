package com.nexacrm.api.controller;

import com.nexacrm.api.dto.PasswordChangeDTO;
import com.nexacrm.api.dto.ProfileUpdateDTO;
import com.nexacrm.api.entity.User;
import com.nexacrm.api.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/v1/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    // Profil məlumatlarını gətir
    @GetMapping("/{id}")
    public ResponseEntity<User> getUser(@PathVariable UUID id) {
        return ResponseEntity.ok(userService.getUserById(id));
    }

    // Profili yenilə
    @PutMapping("/{id}/profile")
    public ResponseEntity<User> updateProfile(@PathVariable UUID id, @RequestBody ProfileUpdateDTO dto) {
        return ResponseEntity.ok(userService.updateProfile(id, dto));
    }

    // Şifrəni dəyiş
    @PutMapping("/{id}/password")
    public ResponseEntity<String> changePassword(@PathVariable UUID id, @RequestBody PasswordChangeDTO dto) {
        userService.changePassword(id, dto);
        return ResponseEntity.ok("Şifrə uğurla yeniləndi");
    }
}