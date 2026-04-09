package com.bankapp.util;

import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import javax.annotation.PostConstruct;
import javax.crypto.SecretKey;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;

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
    public String generateToken(UserDetails userDetails) {
        return Jwts.builder()
                .setSubject(userDetails.getUsername())
                .claim("roles", userDetails.getAuthorities()
                        .stream()
                        .map(GrantedAuthority::getAuthority)
                        .collect(Collectors.toList()))
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + expiration))
                .setIssuer("BankApp")
                .signWith(getSigningKey(), SignatureAlgorithm.HS512)
                .compact();
    }

    // Validate the token (check signature + expiration)
    public boolean validateToken(String token, UserDetails userDetails) {
        try {
            final String username = getUsernameFromToken(token);
            return (username.equals(userDetails.getUsername()) && !isTokenExpired(token));
        } catch (JwtException | IllegalArgumentException e) {
            return false;
        }
    }

    private boolean isTokenExpired(String token) {
        return getClaims(token).getExpiration().before(new Date());
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
    public List<String> getRolesFromToken(String token) {
        Object roles = getClaims(token).get("roles");

        if (roles instanceof List<?>) {
            return ((List<?>) roles).stream()
                    .map(Object::toString)
                    .collect(Collectors.toList());
        }

        return Collections.emptyList(); // fallback
    }
}
