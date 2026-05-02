package com.nexacrm.api.security;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import java.security.Key;
import java.util.Date;
import java.util.UUID;

@Component
public class JwtUtils {
    // Bu açarı çox gizli saxla
	@Value("${app.jwt.secret}") // application.properties-dən oxuyur
    String jwtSecret;
    private final int jwtExpirationMs = 86400000; // 1 günlük ömür

    private Key key() {
        return Keys.hmacShaKeyFor(jwtSecret.getBytes());
    }

    public String generateToken(String email, UUID orgId, String role) {
        return Jwts.builder()
                .setSubject(email)
                .claim("orgId", orgId.toString()) // Şirkət ID-sini biletin içinə qoyuruq
                .claim("role", role)             // Rolu qoyuruq
                .setIssuedAt(new Date())
                .setExpiration(new Date((new Date()).getTime() + jwtExpirationMs))
                .signWith(key(), SignatureAlgorithm.HS256)
                .compact();
    }

    public String getEmailFromToken(String token) {
        return Jwts.parserBuilder().setSigningKey(key()).build()
                .parseClaimsJws(token).getBody().getSubject();
    }

    public boolean validateToken(String token) {
        try {
            Jwts.parserBuilder().setSigningKey(key()).build().parse(token);
            return true;
        } catch (Exception e) {
            return false;
        }
    }
}