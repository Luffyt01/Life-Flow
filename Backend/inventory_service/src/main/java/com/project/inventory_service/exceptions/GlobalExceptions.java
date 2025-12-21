package com.project.inventory_service.exceptions;

import com.project.inventory_service.exceptions.ExceptionTypes.ResourceNotFoundException;
import com.project.inventory_service.exceptions.ExceptionTypes.RuntimeConflictException;
import com.project.inventory_service.exceptions.ExceptionTypes.ErrorInSavingDataInDatabase;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptions {


    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ApiResponse<?>> handleResourceNotFoundException(ResourceNotFoundException exception){
        ApiError error = ApiError.builder()
                .status(HttpStatus.NOT_FOUND)
                .message(exception.getLocalizedMessage())
                .build();
        return buildApiErrorResponseEntity(error);
    }

    @ExceptionHandler(RuntimeConflictException.class)
    public ResponseEntity<ApiResponse<?>> handleRuntimeConflictException(RuntimeException ex){
        ApiError error = ApiError.builder()
                .status(HttpStatus.CONFLICT)
                .message(ex.getLocalizedMessage())
                .build();
        return buildApiErrorResponseEntity(error);
    }
    @ExceptionHandler(ErrorInSavingDataInDatabase.class)
    public ResponseEntity<ApiResponse<?>> handleErrorWhileSaveDataInDatabase(ErrorInSavingDataInDatabase ex){
        ApiError error = ApiError.builder()
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .message(ex.getLocalizedMessage())
                .build();
        return buildApiErrorResponseEntity(error);
    }



    /**
     * Builds a consistent error response entity
     * @param error The ApiError containing status and message
     * @return ResponseEntity with the error response
     */
    private ResponseEntity<ApiResponse<?>> buildApiErrorResponseEntity(ApiError error) {
        return new ResponseEntity<>(new ApiResponse<>(error), error.getStatus());
    }
}
