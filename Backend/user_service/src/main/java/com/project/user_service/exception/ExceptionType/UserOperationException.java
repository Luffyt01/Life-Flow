package com.project.user_service.exception.ExceptionType;


public class UserOperationException extends RuntimeException {
    public UserOperationException(String message) {
        super(message);
    }
}