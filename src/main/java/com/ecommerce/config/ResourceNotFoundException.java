package com.ecommerce.config;

/**
 * Exception thrown when a requested resource is not found.
 * Maps to HTTP 404.
 */
public class ResourceNotFoundException extends RuntimeException {

    public ResourceNotFoundException(String message) {
        super(message);
    }
}
