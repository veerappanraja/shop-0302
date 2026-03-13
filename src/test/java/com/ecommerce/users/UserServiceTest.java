package com.ecommerce.users;

import com.ecommerce.config.BadRequestException;
import com.ecommerce.config.ResourceNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    private UserService userService;

    @BeforeEach
    void setUp() {
        userService = new UserService(userRepository);
    }

    @Test
    void createUser_success() {
        User user = new User("test@example.com", "Test");
        User saved = new User("test@example.com", "Test");
        saved.setId(1);

        when(userRepository.findByEmail("test@example.com")).thenReturn(Optional.empty());
        when(userRepository.save(any(User.class))).thenReturn(saved);

        User result = userService.createUser(user);

        assertEquals(1, result.getId());
        assertEquals("test@example.com", result.getEmail());
        verify(userRepository).save(user);
    }

    @Test
    void createUser_duplicateEmail_throwsBadRequest() {
        User existing = new User("dup@example.com", "Existing");
        User newUser = new User("dup@example.com", "New");

        when(userRepository.findByEmail("dup@example.com")).thenReturn(Optional.of(existing));

        BadRequestException ex = assertThrows(BadRequestException.class,
                () -> userService.createUser(newUser));
        assertEquals("Email already registered", ex.getMessage());
        verify(userRepository, never()).save(any());
    }

    @Test
    void getUserById_found() {
        User user = new User("alice@example.com", "Alice");
        user.setId(1);
        when(userRepository.findById(1)).thenReturn(Optional.of(user));

        User result = userService.getUserById(1);
        assertEquals("Alice", result.getName());
    }

    @Test
    void getUserById_notFound_throwsResourceNotFound() {
        when(userRepository.findById(999)).thenReturn(Optional.empty());

        ResourceNotFoundException ex = assertThrows(ResourceNotFoundException.class,
                () -> userService.getUserById(999));
        assertEquals("User not found", ex.getMessage());
    }

    @Test
    void listUsers_returnsAll() {
        List<User> users = List.of(
                new User("a@example.com", "A"),
                new User("b@example.com", "B")
        );
        when(userRepository.findAll()).thenReturn(users);

        List<User> result = userService.listUsers();
        assertEquals(2, result.size());
    }
}
