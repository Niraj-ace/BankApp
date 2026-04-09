package com.bankapp.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.bankapp.dto.UserRequestDTO;
import com.bankapp.dto.UserResponseDTO;
import com.bankapp.service.UserService;
import com.bankapp.util.JwtUtil;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final UserService userService;
    private final JwtUtil jwtUtil;
    private final AuthenticationManager authenticationManager;

    public AuthController(UserService userService,
    								  JwtUtil jwtUtil,
						  AuthenticationManager authenticationManager) {
        this.userService = userService;
        this.jwtUtil = jwtUtil;
        this.authenticationManager = authenticationManager;
    }

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody UserRequestDTO requestDTO) {
        UserResponseDTO savedUser = userService.registerUser(requestDTO);
        return ResponseEntity.ok(savedUser);
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody UserRequestDTO requestDTO) {
    	Authentication authentication = authenticationManager.authenticate(
    			new UsernamePasswordAuthenticationToken(
                    requestDTO.getName(),
                    requestDTO.getPassword()
                )
            );

         UserDetails userDetails = (UserDetails) authentication.getPrincipal();
         String token = null;
         try {
        	 token = jwtUtil.generateToken(userDetails);
         }catch (Exception e) {
        	    e.printStackTrace();
        	}

        return ResponseEntity.ok(token);
    }
}
