package com.ecommerce.config;

/**
 * Exception thrown when a business rule is violated.
 * Maps to HTTP 400.
 */
public class BusinessRuleException extends RuntimeException {

    public BusinessRuleException(String message) {
        super(message);
    }
}
