package com.project.inventory_service.exceptions;

import com.project.inventory_service.exceptions.ExceptionTypes.ResourceNotFoundException;
import com.project.inventory_service.exceptions.ExceptionTypes.RuntimeConflictException;
import com.project.inventory_service.exceptions.ExceptionTypes.ErrorInSavingDataInDatabase;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.RequestParamMethodArgumentResolver;

import java.util.List;
import java.util.stream.Collectors;

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
    @ExceptionHandler(MissingServletRequestParameterException.class)
    public ResponseEntity<ApiResponse<?>> handleErrorWhileSaveDataInDatabase(MissingServletRequestParameterException ex){
        ApiError error = ApiError.builder()
                .status(HttpStatus.BAD_REQUEST)
                .message(ex.getLocalizedMessage())
                .build();
        return buildApiErrorResponseEntity(error);
    }
//    @ExceptionHandler(ErrorInSavingDataInDatabase.class)
//    public ResponseEntity<ApiResponse<?>> handleErrorWhileSaveDataInDatabase(ErrorInSavingDataInDatabase ex){
//        ApiError error = ApiError.builder()
//                .status(HttpStatus.INTERNAL_SERVER_ERROR)
//                .message(ex.getLocalizedMessage())
//                .build();
//        return buildApiErrorResponseEntity(error);
//    }

    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<ApiResponse<?>> handleDataIntegrityViolationException(DataIntegrityViolationException ex){
        String message = "Database error: " + ex.getMostSpecificCause().getMessage();
        if (ex.getMessage().contains("duplicate key value")) {
            message = "Duplicate entry detected. Please ensure unique values for unique fields.";
        }
        ApiError error = ApiError.builder()
                .status(HttpStatus.CONFLICT)
                .message(message)
                .build();
        return buildApiErrorResponseEntity(error);
    }


    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiResponse<?>> handleInputValidationErrors(MethodArgumentNotValidException exception) {
        List<String> errors = exception
                .getBindingResult()
                .getAllErrors()
                .stream()
                .map(error -> error.getDefaultMessage())
                .collect(Collectors.toList());

        ApiError apiError = ApiError.builder()
                .status(HttpStatus.BAD_REQUEST)
                .message("Input validation failed")
                .subErrors(errors)
                .build();
        return buildApiErrorResponseEntity(apiError);
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
