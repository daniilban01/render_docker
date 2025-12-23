package com.example.service;

import com.example.model.User;
import com.example.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserService userService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void getAllUsers_ShouldReturnUserList() {
        // Mock behavior
        when(userRepository.findAll()).thenReturn(Arrays.asList(
            new User("John", "john@example.com"),
            new User("Jane", "jane@example.com")
        ));

        // Execute
        List<User> users = userService.getAllUsers();

        // Verify
        assertEquals(2, users.size());
        assertEquals("John", users.get(0).getName());
        verify(userRepository, times(1)).findAll();
    }

    @Test
    void getUserById_ShouldReturnUser() {
        User mockUser = new User("John", "john@example.com");
        when(userRepository.findById(1L)).thenReturn(Optional.of(mockUser));

        User user = userService.getUserById(1L);

        assertEquals("John", user.getName());
        verify(userRepository, times(1)).findById(1L);
    }
}
