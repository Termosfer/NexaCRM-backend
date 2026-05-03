package com.nexacrm.api.repository;

import com.nexacrm.api.entity.PasswordResetToken;
import com.nexacrm.api.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface PasswordResetTokenRepository extends JpaRepository<PasswordResetToken, Long> {
    
    // Tokenə görə bazadan axtarış etmək üçün
    Optional<PasswordResetToken> findByToken(String token);
    
    // Köhnə tokenləri təmizləmək üçün
    void deleteByUser(User user);
}