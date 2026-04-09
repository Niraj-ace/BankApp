package com.bankapp.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.test.web.servlet.MockMvc;

import com.bankapp.dto.UserRequestDTO;
import com.bankapp.dto.UserResponseDTO;
import com.bankapp.service.UserService;
import com.bankapp.util.JwtUtil;
import com.fasterxml.jackson.databind.ObjectMapper;

@WebMvcTest(AuthController.class)
@AutoConfigureMockMvc(addFilters = false)
class AuthControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UserService userService;

    @MockBean
    private JwtUtil jwtUtil; // 🔥 ADD THIS
    
    @MockBean
    private AuthenticationManager authenticationManager;

    @Autowired
    private ObjectMapper objectMapper;
    
    private UserRequestDTO request;
    private UserResponseDTO response;
    
    @BeforeEach
    void setup() {
    	request = new UserRequestDTO();
        request.setName("alice");
        request.setPassword("password");

        response = new UserResponseDTO();
        response.setId(1L);
	}

    // 🔥 Test REGISTER
    @Test
    void testRegister_Success() throws Exception {

        when(userService.registerUser(any(UserRequestDTO.class)))
                .thenReturn(response);

        mockMvc.perform(post("/api/auth/register")
                .contentType("application/json")
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L));
    }

    // 🔥 Test LOGIN
    @Test
    void testLogin_Success() throws Exception {

    	Authentication authentication = mock(Authentication.class);
	      UserDetails userDetails = mock(UserDetails.class);
	
	      when(authenticationManager.authenticate(any()))
	          .thenReturn(authentication);
	
	      when(authentication.getPrincipal())
	          .thenReturn(userDetails);
	
	      when(jwtUtil.generateToken(userDetails))
	      
	          .thenReturn("dummy-token");
        mockMvc.perform(post("/api/auth/login")
                .contentType("application/json")
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(content().string("dummy-token"));
    }
}