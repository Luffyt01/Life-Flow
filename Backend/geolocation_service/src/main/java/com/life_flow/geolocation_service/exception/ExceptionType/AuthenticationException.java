package com.life_flow.geolocation_service.exception.ExceptionType;

public class AuthenticationException extends RuntimeException {
    public AuthenticationException(String message) {
        super(message);
    }
}