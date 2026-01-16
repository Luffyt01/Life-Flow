package com.project.user_service.exception.ExceptionType;

public class TokenExpireException extends RuntimeException {
    public TokenExpireException() {
    }

    public TokenExpireException(String message) {
        super(message);
    }
}
