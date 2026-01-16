package com.project.user_service.exception;

import com.project.user_service.exception.ExceptionType.*;
import io.jsonwebtoken.JwtException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.session.SessionAuthenticationException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.List;
import java.util.stream.Collectors;

@RestControllerAdvice
public class GlobalExceptionHandler  {

    //This error for Resource not found
    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ApiResponse<?>> handleResourceNotFoundException(ResourceNotFoundException ex){
        ApiError error = ApiError.builder()
                .status(HttpStatus.NOT_FOUND)
                .message(ex.getLocalizedMessage())
                .build();
        return buildApiErrorResponseEntity(error);
    }

    //This error for Runtime Conflict
    @ExceptionHandler(RuntimeConflictException.class)
    public ResponseEntity<ApiResponse<?>> handleRuntimeConflictException(RuntimeConflictException exception) {
        ApiError apiError = ApiError.builder()
                .status(HttpStatus.CONFLICT)
                .message(exception.getLocalizedMessage())
                .build();
        return buildApiErrorResponseEntity(apiError);
    }
    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<ApiResponse<?>> handleRuntimeException(RuntimeException exception) {
        ApiError apiError = ApiError.builder()
                .status(HttpStatus.CONFLICT)
                .message(exception.getLocalizedMessage())
                .build();
        return buildApiErrorResponseEntity(apiError);
    }

    // user operation exception
    @ExceptionHandler(UserOperationException.class)
    public ResponseEntity<ApiResponse<?>>  handleUserOperationException(UserOperationException ex){
        ApiError error = ApiError.builder()
                .status(HttpStatus.NOT_FOUND)
                .message(ex.getLocalizedMessage())
                .build();
        return buildApiErrorResponseEntity(error);
    }

    @ExceptionHandler(AuthenticationException.class)
    public ResponseEntity<ApiResponse<?>> handleAuthenticationException(AuthenticationException ex) {
        ApiError error = ApiError.builder()
                .status(HttpStatus.UNAUTHORIZED)
                .message(ex.getLocalizedMessage())
                .build();
        return buildApiErrorResponseEntity(error);
    }

    @ExceptionHandler(TokenExpireException.class)
    public ResponseEntity<ApiResponse<?>> handleForgetPasswordTokenExpiredException(TokenExpireException ex) {
        ApiError error = ApiError.builder()
                .status(HttpStatus.UNAUTHORIZED)
                .message(ex.getLocalizedMessage())
                .build();
        return buildApiErrorResponseEntity(error);
    }

    @ExceptionHandler(MailSendingErrorException.class)
    public ResponseEntity<ApiResponse<?>> handleMailSendingErrorException(MailSendingErrorException ex) {
        ApiError error = ApiError.builder()
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .message(ex.getLocalizedMessage())
                .build();
        return buildApiErrorResponseEntity(error);
    }

    @ExceptionHandler(JwtException.class)
    public ResponseEntity<ApiResponse<?>> handleJwtException(JwtException ex) {
        ApiError error = ApiError.builder()
                .status(HttpStatus.UNAUTHORIZED)
                .message(ex.getLocalizedMessage())
                .build();
        return buildApiErrorResponseEntity(error);
    }

    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<ApiResponse<?>> handleAccessDeniedException(AccessDeniedException ex) {
        ApiError error = ApiError.builder()
                .status(HttpStatus.FORBIDDEN)
                .message(ex.getLocalizedMessage())
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

    @ExceptionHandler(InvalidTokenException.class)
    public ResponseEntity<ApiError> handleInvalidTokenException(InvalidTokenException e){
        ApiError apiError = ApiError.builder()
                .status(HttpStatus.UNAUTHORIZED)
                .message(e.getLocalizedMessage())
                .build();
        return new ResponseEntity<>(apiError, HttpStatus.UNAUTHORIZED);
    }

    @ExceptionHandler(SessionAuthenticationException.class)
    public ResponseEntity<ApiError> handleSessionAuthenticationException(SessionAuthenticationException e){
        ApiError apiError = ApiError.builder()
                .status(HttpStatus.UNAUTHORIZED)
                .message(e.getLocalizedMessage())
                .build();
        return new ResponseEntity<>(apiError, HttpStatus.UNAUTHORIZED);
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
