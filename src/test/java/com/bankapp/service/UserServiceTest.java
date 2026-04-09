package com.bankapp.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.bankapp.dto.UserRequestDTO;
import com.bankapp.dto.UserResponseDTO;
import com.bankapp.entity.User;
import com.bankapp.mapper.UserMapper;
import com.bankapp.repository.UserRepository;
import com.bankapp.util.JwtUtil;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @InjectMocks
    private UserService userService;

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private UserMapper usermapper;

    @Mock
    private JwtUtil jwtUtil;

    @Mock
    private AuthenticationManager authenticationManager;
    
    private UserRequestDTO request;
    private User user;
    private UserResponseDTO response;

    @BeforeEach
    void setup() {
        request = new UserRequestDTO();
        request.setName("alice");
        request.setPassword("password");

        user = new User();
        user.setUsername("alice");

        response = new UserResponseDTO();
        response.setId(1L);
    }

    @Test
    void testRegisterUser_Success() {
        when(userRepository.existsByUsername(any(String.class))).thenReturn(false);
        when(passwordEncoder.encode(any(String.class))).thenReturn("encodedPassword");

        when(usermapper.mapUserRequestDTOToUser(any(UserRequestDTO.class)))
            .thenReturn(user);

        when(userRepository.save(any(User.class))).thenReturn(user);

        when(usermapper.mapUserToUserResponsetDTO(user))
            .thenReturn(response);

        UserResponseDTO result = userService.registerUser(request);

        assertNotNull(result);
        assertEquals(1L, result.getId());

        verify(userRepository).save(any(User.class));
    }
    
    @Test
    void testRegisterUser_AlreadyExists() {
        when(userRepository.existsByUsername("alice")).thenReturn(true);

        RuntimeException ex = assertThrows(RuntimeException.class,
            () -> userService.registerUser(request));

        assertEquals("Username already exists", ex.getMessage());
    }
    
    @Test
    void testLoadUserByUsername_UserExists() {
        when(userRepository.findByUsername("alice")).thenReturn(Optional.of(user));

        UserDetails result = userService.loadUserByUsername("alice");

        assertNotNull(result);
    }
}

