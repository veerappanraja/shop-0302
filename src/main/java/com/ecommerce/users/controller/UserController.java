package com.ecommerce.users.controller;

import com.ecommerce.model.User;
import com.ecommerce.users.dto.CreateUserRequest;
import com.ecommerce.users.dto.UserResponse;
import com.ecommerce.users.service.UserService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * REST controller for user endpoints.
 * Translates users/api.py routes.
 */
@RestController
@RequestMapping("/users")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.OK)
    public UserResponse createUser(@Valid @RequestBody CreateUserRequest request) {
        User user = userService.createUser(request.getEmail(), request.getName());
        return UserResponse.fromEntity(user);
    }

    @GetMapping("/{user_id}")
    public UserResponse getUser(@PathVariable("user_id") Long userId) {
        User user = userService.getUserById(userId);
        return UserResponse.fromEntity(user);
    }

    @GetMapping
    public List<UserResponse> listUsers() {
        return userService.listUsers().stream()
                .map(UserResponse::fromEntity)
                .toList();
    }
}
