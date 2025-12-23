package com.project.Life_Flow.donor_service.exception;

import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtException; // Import for general JWT errors
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.orm.jpa.JpaSystemException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ApiError> handleResourceNotFoundException(ResourceNotFoundException exception){
        // 404: The resource requested does not exist
        return buildErrorResponse(exception, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(BadRequestException.class)
    public ResponseEntity<ApiError> handleBadRequestException(BadRequestException exception) {
        // 400: The client sent invalid data
        return buildErrorResponse(exception, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<ApiError> handleHttpsMsgNotReadable(HttpMessageNotReadableException exception){
        // 400: The JSON body is malformed (e.g., missing brackets, wrong types)
        return buildErrorResponse(exception, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(IllegalStateException.class)
    public ResponseEntity<ApiError> handleIllegalStateException(IllegalStateException exception){
        // 409: Conflict. Best for "Profile already exists" or invalid business state transitions
        return buildErrorResponse(exception, HttpStatus.CONFLICT);
    }

    @ExceptionHandler({ExpiredJwtException.class, JwtException.class})
    public ResponseEntity<ApiError> handleJwtExceptions(Exception exception){
        // 401: Unauthorized. The token is invalid or expired
        return buildErrorResponse(exception, HttpStatus.UNAUTHORIZED);
    }

    @ExceptionHandler(JpaSystemException.class)
    public ResponseEntity<ApiError> handleJpaSystemException(JpaSystemException exception){
        // 500: Internal Server Error (Database issues, LOB errors)
        return buildErrorResponse(exception, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    public ResponseEntity<ApiError> buildErrorResponse(Exception exception, HttpStatus status){
        ApiError apiError = new ApiError(exception.getLocalizedMessage(), status);
        return new ResponseEntity<>(apiError, status);
    }
}