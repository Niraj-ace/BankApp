package com.bankapp;

import com.bankapp.entity.User;
import com.bankapp.repository.UserRepository;
import com.bankapp.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class UserServiceTest {

    @InjectMocks
    private UserService userService;

    @Mock
    private UserRepository userRepo;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testRegisterUser_Success() {
        User user = new User();
        user.setUsername("alice");
        user.setPassword("password");

        when(userRepo.existsByUsername("alice")).thenReturn(false);
        when(userRepo.save(any(User.class))).thenAnswer(i -> i.getArguments()[0]);

//        User savedUser = userService.registerUser(user);
//        assertEquals("alice", savedUser.getUsername());
        verify(userRepo, times(1)).save(user);
    }

    @Test
    public void testFindByUsername_UserExists() {
        User user = new User();
        user.setUsername("bob");

        when(userRepo.findByUsername("bob")).thenReturn(Optional.of(user));

//        Optional<User> found = userService.findByUsername("bob");
//        assertTrue(found.isPresent());
//        assertEquals("bob", found.get().getUsername());
    }
}
