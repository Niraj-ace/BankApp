package com.bankapp.service;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.bankapp.dto.UserRequestDTO;
import com.bankapp.dto.UserResponseDTO;
import com.bankapp.entity.User;
import com.bankapp.mapper.UserMapper;
import com.bankapp.repository.UserRepository;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class UserService implements UserDetailsService{

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    @Autowired
    private UserMapper usermapper;

    public UserService(UserRepository userRepository,
                       PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public UserResponseDTO registerUser(UserRequestDTO request) {
        if (userRepository.existsByUsername(request.getName())) {
        	log.error("User - {} already exists" ,request.getName());
            throw new RuntimeException("Username already exists");
        }
        request.setPassword(passwordEncoder.encode(request.getPassword()));
        User user = usermapper.mapUserRequestDTOToUser(request);
        userRepository.save(user);
        UserResponseDTO response = usermapper.mapUserToUserResponsetDTO(user);
        log.info("UserID {} registered", response.getId());
        return response;
    }

    public Optional<User> findByUsername(String username) {
        return userRepository.findByUsername(username);
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return userRepository.findByUsername(username)
            .orElseThrow(() -> new UsernameNotFoundException("User - "+username+" not found"));
    }

}
