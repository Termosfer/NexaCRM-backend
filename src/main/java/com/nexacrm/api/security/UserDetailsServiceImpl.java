package com.nexacrm.api.security;

import com.nexacrm.api.entity.User;
import com.nexacrm.api.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;

@Service
@RequiredArgsConstructor
public class UserDetailsServiceImpl implements UserDetailsService {

    private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        // İstifadəçini email ilə bazadan tapırıq
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("İstifadəçi tapılmadı: " + email));

        // Spring Security-nin başqa bir "User" obyekti var, ona çeviririk
        return new org.springframework.security.core.userdetails.User(
                user.getEmail(),
                user.getPassword(),
                new ArrayList<>() // Burada rolları da əlavə edə bilərik (məs: Role.ADMIN)
        );
    }
}