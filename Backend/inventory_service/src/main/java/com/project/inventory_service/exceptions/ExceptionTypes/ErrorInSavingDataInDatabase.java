package com.project.inventory_service.exceptions.ExceptionTypes;

public class ErrorInSavingDataInDatabase extends  RuntimeException{
    public ErrorInSavingDataInDatabase(String message) {
        super(message);
    }
}
