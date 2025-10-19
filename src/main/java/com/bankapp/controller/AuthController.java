package com.bankapp.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.bankapp.dto.UserRequestDTO;
import com.bankapp.dto.UserResponseDTO;
import com.bankapp.entity.User;
import com.bankapp.service.UserService;
import com.bankapp.util.JwtUtil;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final UserService userService;
    private final JwtUtil jwtUtil;
    private final PasswordEncoder passwordEncoder;

    public AuthController(UserService userService, JwtUtil jwtUtil, PasswordEncoder passwordEncoder) {
        this.userService = userService;
        this.jwtUtil = jwtUtil;
        this.passwordEncoder = passwordEncoder;
        
    }

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody UserRequestDTO requestDTO) {
        UserResponseDTO savedUser = userService.registerUser(requestDTO);
        return ResponseEntity.ok(savedUser);
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody UserRequestDTO requestDTO) {
        User found = userService.findByUsername(requestDTO.getName())
                .orElseThrow(() -> new RuntimeException("User not found"));
        
        if (!passwordEncoder.matches(requestDTO.getPassword(), found.getPassword())) {
            throw new RuntimeException("Invalid credentials");
        }

        String token = jwtUtil.generateToken(found);
        return ResponseEntity.ok(token);
    }
}
