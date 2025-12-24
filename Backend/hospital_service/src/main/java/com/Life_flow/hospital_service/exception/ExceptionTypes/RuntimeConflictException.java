package com.Life_flow.hospital_service.exception.ExceptionTypes;

public class RuntimeConflictException extends  RuntimeException{
    public RuntimeConflictException() {
    }

    public RuntimeConflictException(String message) {
        super(message);
    }
}
