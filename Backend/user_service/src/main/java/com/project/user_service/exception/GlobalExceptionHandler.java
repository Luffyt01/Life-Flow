package com.project.user_service.exception;

import com.project.user_service.exception.ExceptionType.AuthenticationException;
import com.project.user_service.exception.ExceptionType.ResourceNotFoundException;

import com.project.user_service.exception.ExceptionType.RuntimeConflictException;
import com.project.user_service.exception.ExceptionType.UserOperationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler  {

    //This error for Resource not found
    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ApiResponse<?>> handleResourceNotFoundException(ResourceNotFoundException ex){
        ApiError error = ApiError.builder()
                .status(HttpStatus.NOT_FOUND)
                .message(ex.getMessage())
                .build();
        return buildApiErrorResponseEntity(error);
    }

    //This error for Runtime Conflict
    @ExceptionHandler(RuntimeConflictException.class)
    public ResponseEntity<ApiResponse<?>> handleRuntimeConflictException(RuntimeConflictException exception) {
        ApiError apiError = ApiError.builder()
                .status(HttpStatus.CONFLICT)
                .message(exception.getMessage())
                .build();
        return buildApiErrorResponseEntity(apiError);
    }

    // user operation exception
    @ExceptionHandler(UserOperationException.class)
    public ResponseEntity<ApiResponse<?>>  handleUserOperationException(UserOperationException ex){
        ApiError error = ApiError.builder()
                .status(HttpStatus.FOUND)
                .message(ex.getMessage())
                .build();
        return buildApiErrorResponseEntity(error);
    }

    @ExceptionHandler(AuthenticationException.class)
    public ResponseEntity<ApiResponse<?>> handleAuthenticationException(AuthenticationException ex){
        ApiError error = ApiError.builder()
                .status(HttpStatus.UNAUTHORIZED)
                .message(ex.getMessage())
                .build();
        return buildApiErrorResponseEntity(error);
    }

    //This is for build error response
    private ResponseEntity<ApiResponse<?>> buildApiErrorResponseEntity(ApiError error) {
        return  new ResponseEntity<>(new ApiResponse<>(error),error.getStatus());
    }
}
