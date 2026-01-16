package com.life_flow.geolocation_service.exception.ExceptionType;

public class InvalidTokenException extends RuntimeException {
    public InvalidTokenException(String message) {
        super(message);
    }
}