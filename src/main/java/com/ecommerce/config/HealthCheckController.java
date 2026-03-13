package com.ecommerce.config;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
public class HealthCheckController {

    @GetMapping("/")
    public Map<String, String> healthCheck() {
        return Map.of(
                "status", "healthy",
                "service", "ecommerce-monolith"
        );
    }
}
