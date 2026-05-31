package com.Project.QuickHost.advice;


import com.Project.QuickHost.exception.ConflictException;
import com.Project.QuickHost.exception.ResourceNotFoundException;
import io.jsonwebtoken.JwtException;
import org.apache.coyote.BadRequestException;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import org.springframework.web.client.HttpServerErrorException.InternalServerError;

import javax.naming.AuthenticationException;
import org.springframework.security.access.AccessDeniedException;
import lombok.extern.slf4j.Slf4j;

@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {//works with controller and service (dispatcher servelet0

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ApiResponse<?>> handleResourceNotFoundException(ResourceNotFoundException ex) {
        ApiError error = ApiError.builder()
                .status(HttpStatus.NOT_FOUND)
                .message(ex.getMessage())
                .build();
        return new ResponseEntity<>(new ApiResponse<>(error), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(BadRequestException.class)
    public ResponseEntity<ApiResponse<?>> handleBadRequestException(BadRequestException ex) {
        ApiError error = ApiError.builder()
                .status(HttpStatus.BAD_REQUEST)
                .message(ex.getMessage())
                .build();
        return new ResponseEntity<>(new ApiResponse<>(error), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(AuthenticationException.class)
    public ResponseEntity<ApiResponse<?>> handleAuthenticationException(AuthenticationException ex) {
        ApiError error = ApiError.builder()
                .status(HttpStatus.UNAUTHORIZED)
                .message(ex.getMessage())
                .build();
        return new ResponseEntity<>(new ApiResponse<>(error), HttpStatus.UNAUTHORIZED);
    }



    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiResponse<?>> handleGenericException(Exception ex) {
        log.error("Unhandled exception reached generic handler", ex);
        ApiError error = ApiError.builder()
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .message("An unexpected error occurred: " + ex.getMessage())
                .build();
        return new ResponseEntity<>(new ApiResponse<>(error), HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(org.springframework.security.core.AuthenticationException.class)
    public ResponseEntity<ApiResponse<?>> handleAuthenticationException(org.springframework.security.core.AuthenticationException ex) {
        ApiError apiError = ApiError.builder()
                .status(HttpStatus.UNAUTHORIZED)
                .message(ex.getMessage())
                .build();
        return new ResponseEntity<>(new ApiResponse<>(apiError), HttpStatus.UNAUTHORIZED);
    }

    @ExceptionHandler(JwtException.class)
    public ResponseEntity<ApiResponse<?>> handleJwtException(JwtException ex) {
        ApiError apiError = ApiError.builder()
                .status(HttpStatus.UNAUTHORIZED)
                .message(ex.getMessage())
                .build();
        return new ResponseEntity<>(new ApiResponse<>(apiError), HttpStatus.UNAUTHORIZED);
    }

    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<ApiResponse<?>> handleAccessDeniedException(org.springframework.security.access.AccessDeniedException ex) {
        ApiError apiError = ApiError.builder()
                .status(HttpStatus.FORBIDDEN)
                .message(ex.getMessage())
                .build();
        return new ResponseEntity<>(new ApiResponse<>(apiError), HttpStatus.FORBIDDEN);
    }
    @ExceptionHandler(InternalServerError.class)
    public ResponseEntity<ApiResponse<?>> handleInternalException(Exception exception) {
        ApiError error = ApiError.builder()
                .message(("Internal  Error") + exception.getMessage())
                .build();
        return  new ResponseEntity<>(new ApiResponse<>(error), HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(ConflictException.class)
    public ResponseEntity<ApiResponse<?>>handleConflictException(ConflictException ex) {
        ApiError error = ApiError.builder()
                .status(HttpStatus.CONFLICT)
                .message("Conflic exception: " + ex.getMessage())
                .build();
        return new ResponseEntity<>(new ApiResponse<>(error), HttpStatus.CONFLICT);
    }

}
