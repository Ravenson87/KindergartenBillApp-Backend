package com.example.KindergartenBillApp.sharedTools.exceptions;


import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.ConstraintViolationException;
import org.springframework.dao.DataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;

/**
 * GlobalExceptionHandler centralizes exception handling across the application.
 * Converts different types of exceptions into REST-friendly JSON responses
 * with appropriate HTTP status codes and request path details.
 */
@RestControllerAdvice
public class GlobalExceptionHandler {


    /**
     * Handles CustomException thrown in the application.
     * Returns a BAD_REQUEST (400) status with the error message and request path.
     */
    @ExceptionHandler(CustomException.class)
    public ResponseEntity<Map<String, Object>> handleCustomException(
            CustomException ex,
            HttpServletRequest request) {
        Map<String, Object> error = new HashMap<>();
        error.put("message", ex.getMessage());
        error.put("path", request.getRequestURI());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
    }

    /**
     * Handles validation errors from @Valid annotated request bodies.
     * Returns a BAD_REQUEST (400) status with field-specific error messages and request path.
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, Object>> handleValidationException(
            MethodArgumentNotValidException ex,
            HttpServletRequest request) {
        Map<String, Object> error = new HashMap<>();
        ex.getBindingResult().getFieldErrors().forEach((err) -> error.put(err.getField(), err.getDefaultMessage()));
        error.put("path", request.getRequestURI());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
    }

    /**
     * Handles type mismatch errors in request parameters.
     * Returns a BAD_REQUEST (400) status with a descriptive message about the invalid type.
     */
    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity<Map<String, Object>> handleTypeMismatchException(
            MethodArgumentTypeMismatchException ex,
            HttpServletRequest request) {
        Map<String, Object> error = new HashMap<>();
        if (LocalDate.class.equals(ex.getRequiredType())) {
            error.put("message", "Invalid date. Expected format yyyy-MM-dd and a valid calendar date.");
        } else {
            error.put("message", "Invalid parameter type: " + ex.getMessage());
        }
        error.put("path", request.getRequestURI());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
    }

    /**
     * Handles constraint violations from @Validated annotations.
     * Returns a BAD_REQUEST (400) status with field-specific validation messages and request path.
     */
    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<Map<String, Object>> handleConstraintViolation(
            ConstraintViolationException ex,
            HttpServletRequest request) {
        Map<String, Object> error = new HashMap<>();
        ex.getConstraintViolations().forEach((violation) -> {
            String field = violation.getPropertyPath().toString();
            String message = violation.getMessage();
            error.put(field, message);
        });
        error.put("path", request.getRequestURI());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
    }

    /**
     * Handles unexpected exceptions not covered by specific handlers.
     * Returns an INTERNAL_SERVER_ERROR (500) status with a generic error message and request path.
     */
    @ExceptionHandler(Exception.class)
    public ResponseEntity<Map<String, Object>> handleGeneralException(
            Exception ex,
            HttpServletRequest request) {
        Map<String, Object> error = new HashMap<>();
        error.put("message", "Unexpected error: " + ex.getMessage());
        error.put("path", request.getRequestURI());
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
    }

    /**
     * Handles ApiExceptions thrown in the service layer.
     * Returns the status defined in the exception with the error message and request path.
     */
    @ExceptionHandler(ApiExceptions.class)
    public ResponseEntity<Map<String, Object>> handleApiException(
            ApiExceptions ex,
            HttpServletRequest request) {
        Map<String, Object> error = new HashMap<>();
        error.put("message", ex.getMessage());
        error.put("path", request.getRequestURI());
        return ResponseEntity.status(ex.getStatus()).body(error);
    }

    /**
     * Handles database-related exceptions such as connectivity or query errors.
     * Returns an INTERNAL_SERVER_ERROR (500) status with the database error details and request path.
     */
    @ExceptionHandler(DataAccessException.class)
    public ResponseEntity<Map<String, Object>> handleDatabaseException(
            DataAccessException ex,
            HttpServletRequest request) {
        Map<String, Object> error = new HashMap<>();
        error.put("message", "Database error: " + ex.getMostSpecificCause().getMessage());
        error.put("path", request.getRequestURI());
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
    }


}
