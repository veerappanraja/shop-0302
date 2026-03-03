package com.ecommerce.users;

import com.ecommerce.model.User;
import com.ecommerce.users.repository.UserRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Map;

import static org.hamcrest.Matchers.hasSize;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {
        userRepository.deleteAll();
    }

    @Test
    void createUser_success() throws Exception {
        Map<String, String> request = Map.of("email", "john@example.com", "name", "John Doe");

        mockMvc.perform(post("/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").isNumber())
                .andExpect(jsonPath("$.email").value("john@example.com"))
                .andExpect(jsonPath("$.name").value("John Doe"))
                .andExpect(jsonPath("$.created_at").isNotEmpty());
    }

    @Test
    void createUser_duplicateEmail_returns400() throws Exception {
        // Create first user
        User user = new User("dup@example.com", "First User");
        userRepository.save(user);

        // Attempt to create second user with same email
        Map<String, String> request = Map.of("email", "dup@example.com", "name", "Second User");

        mockMvc.perform(post("/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.detail").value("Email already registered"));
    }

    @Test
    void getUser_success() throws Exception {
        User user = new User("jane@example.com", "Jane Doe");
        user = userRepository.save(user);

        mockMvc.perform(get("/users/{user_id}", user.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(user.getId()))
                .andExpect(jsonPath("$.email").value("jane@example.com"))
                .andExpect(jsonPath("$.name").value("Jane Doe"))
                .andExpect(jsonPath("$.created_at").isNotEmpty());
    }

    @Test
    void getUser_notFound_returns404() throws Exception {
        mockMvc.perform(get("/users/{user_id}", 999))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.detail").value("User not found"));
    }

    @Test
    void listUsers_empty() throws Exception {
        mockMvc.perform(get("/users"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(0)));
    }

    @Test
    void listUsers_returnsAllUsers() throws Exception {
        userRepository.save(new User("alice@example.com", "Alice"));
        userRepository.save(new User("bob@example.com", "Bob"));

        mockMvc.perform(get("/users"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].email").isNotEmpty())
                .andExpect(jsonPath("$[1].email").isNotEmpty());
    }

    @Test
    void createUser_missingEmail_returns400() throws Exception {
        Map<String, String> request = Map.of("name", "No Email User");

        mockMvc.perform(post("/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.detail").isNotEmpty());
    }

    @Test
    void createUser_missingName_returns400() throws Exception {
        Map<String, String> request = Map.of("email", "valid@example.com");

        mockMvc.perform(post("/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.detail").isNotEmpty());
    }
}
