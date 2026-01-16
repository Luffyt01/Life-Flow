package com.project.inventory_service.exceptions.ExceptionTypes;

public class RuntimeConflictException extends  RuntimeException{
    public RuntimeConflictException() {
    }

    public RuntimeConflictException(String message) {
        super(message);
    }
}
