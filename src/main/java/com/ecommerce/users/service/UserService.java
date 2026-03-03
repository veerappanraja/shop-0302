package com.ecommerce.users.service;

import com.ecommerce.config.BusinessRuleException;
import com.ecommerce.config.ResourceNotFoundException;
import com.ecommerce.model.User;
import com.ecommerce.users.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Service encapsulating user business logic.
 * Translates logic from users/api.py.
 */
@Service
public class UserService {

    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Transactional
    public User createUser(String email, String name) {
        // Mimic SQLModel behavior: null fields cause a database-level error (500)
        // Check BEFORE save to avoid consuming auto-increment IDs
        if (email == null || name == null) {
            throw new RuntimeException("NOT NULL constraint failed");
        }

        // Check if email already exists
        if (userRepository.findByEmail(email).isPresent()) {
            throw new BusinessRuleException("Email already registered");
        }

        User user = new User(email, name);
        return userRepository.save(user);
    }

    @Transactional(readOnly = true)
    public User getUserById(Long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));
    }

    @Transactional(readOnly = true)
    public List<User> listUsers() {
        return userRepository.findAll();
    }
}
