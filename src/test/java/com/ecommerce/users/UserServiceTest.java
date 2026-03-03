package com.ecommerce.users;

import com.ecommerce.config.BusinessRuleException;
import com.ecommerce.config.ResourceNotFoundException;
import com.ecommerce.model.User;
import com.ecommerce.users.repository.UserRepository;
import com.ecommerce.users.service.UserService;
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
        when(userRepository.findByEmail("test@example.com")).thenReturn(Optional.empty());
        User savedUser = new User("test@example.com", "Test User");
        savedUser.setId(1L);
        when(userRepository.save(any(User.class))).thenReturn(savedUser);

        User result = userService.createUser("test@example.com", "Test User");

        assertNotNull(result);
        assertEquals(1L, result.getId());
        assertEquals("test@example.com", result.getEmail());
        assertEquals("Test User", result.getName());
        verify(userRepository).findByEmail("test@example.com");
        verify(userRepository).save(any(User.class));
    }

    @Test
    void createUser_duplicateEmail_throwsException() {
        User existing = new User("dup@example.com", "Existing");
        when(userRepository.findByEmail("dup@example.com")).thenReturn(Optional.of(existing));

        BusinessRuleException exception = assertThrows(BusinessRuleException.class,
                () -> userService.createUser("dup@example.com", "New User"));

        assertEquals("Email already registered", exception.getMessage());
        verify(userRepository).findByEmail("dup@example.com");
        verify(userRepository, never()).save(any());
    }

    @Test
    void getUserById_success() {
        User user = new User("found@example.com", "Found User");
        user.setId(1L);
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));

        User result = userService.getUserById(1L);

        assertNotNull(result);
        assertEquals(1L, result.getId());
        verify(userRepository).findById(1L);
    }

    @Test
    void getUserById_notFound_throwsException() {
        when(userRepository.findById(999L)).thenReturn(Optional.empty());

        ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class,
                () -> userService.getUserById(999L));

        assertEquals("User not found", exception.getMessage());
        verify(userRepository).findById(999L);
    }

    @Test
    void listUsers_returnsAll() {
        List<User> users = List.of(
                new User("a@example.com", "Alice"),
                new User("b@example.com", "Bob")
        );
        when(userRepository.findAll()).thenReturn(users);

        List<User> result = userService.listUsers();

        assertEquals(2, result.size());
        verify(userRepository).findAll();
    }

    @Test
    void listUsers_empty() {
        when(userRepository.findAll()).thenReturn(List.of());

        List<User> result = userService.listUsers();

        assertTrue(result.isEmpty());
        verify(userRepository).findAll();
    }
}
