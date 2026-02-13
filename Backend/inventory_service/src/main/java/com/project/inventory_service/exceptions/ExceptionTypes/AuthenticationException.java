package com.project.inventory_service.exceptions.ExceptionTypes;

public class AuthenticationException extends RuntimeException {
    public AuthenticationException(String message) {
        super(message);
    }
}