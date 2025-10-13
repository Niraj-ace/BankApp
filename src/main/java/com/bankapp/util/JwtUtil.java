package com.bankapp.util;

import com.bankapp.entity.User;
import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.crypto.SecretKey;
import java.util.Date;

@Component
public class JwtUtil {

    @Value("${jwt.secret}")
    private String secret;   // should be at least 64 chars for HS512

    @Value("${jwt.expiration}")
    private long expiration; // in milliseconds

    // Create a strong signing key
    private SecretKey getSigningKey() {
        byte[] keyBytes = secret.getBytes();
        System.out.println("🔐 Using key bytes length: " + keyBytes.length);
        if (keyBytes.length < 64) {
            throw new IllegalStateException("❌ JWT secret too short! Found only " + keyBytes.length + " bytes.");
        }
        return Keys.hmacShaKeyFor(keyBytes);
    }
    
    @PostConstruct
    public void checkKeyLength() {
        System.out.println("🔐 JWT Secret Length (chars): " + secret.length());
        System.out.println("🔐 JWT Secret Bytes: " + secret.getBytes().length);
    }

    // Generate JWT token
    public String generateToken(User user) {
        return Jwts.builder()
                .setSubject(user.getUsername())
                .claim("role", user.getRole().name())
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + expiration))
                .setIssuer("BankApp")
                .signWith(getSigningKey(), SignatureAlgorithm.HS512)
                .compact();
    }

    // Validate the token (check signature + expiration)
    public boolean validateToken(String token) {
        try {
            Jwts.parserBuilder()
                    .setSigningKey(getSigningKey())
                    .build()
                    .parseClaimsJws(token);
            return true;
        } catch (JwtException | IllegalArgumentException e) {
            return false; // token expired, malformed, or invalid
        }
    }

    // Extract all claims
    public Claims getClaims(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(getSigningKey())
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    // Extract username
    public String getUsernameFromToken(String token) {
        return getClaims(token).getSubject();
    }

    // Extract role
    public String getRoleFromToken(String token) {
        return getClaims(token).get("role", String.class);
    }
}
