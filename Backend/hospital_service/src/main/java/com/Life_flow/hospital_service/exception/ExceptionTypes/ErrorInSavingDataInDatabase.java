package com.Life_flow.hospital_service.exception.ExceptionTypes;

public class ErrorInSavingDataInDatabase extends  RuntimeException{
    public ErrorInSavingDataInDatabase(String message) {
        super(message);
    }
}
