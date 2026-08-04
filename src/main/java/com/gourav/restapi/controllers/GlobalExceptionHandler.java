package com.gourav.restapi.controllers;

import com.gourav.restapi.controllers.payload.response.ErrorResponse;
import com.gourav.restapi.exceptions.RoleNotFoundException;
import jakarta.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.server.ResponseStatusException;

import java.util.Map;
import java.util.stream.Collectors;

@RestControllerAdvice
public class GlobalExceptionHandler {

    private static final Logger logger = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handleValidation(MethodArgumentNotValidException exception,
                                                          HttpServletRequest request) {
        Map<String, String> fieldErrors = exception.getBindingResult()
                .getFieldErrors()
                .stream()
                .collect(Collectors.toMap(
                        FieldError::getField,
                        fieldError -> fieldError.getDefaultMessage() == null ? "Invalid value" : fieldError.getDefaultMessage(),
                        (first, second) -> first));

        return buildResponse(HttpStatus.BAD_REQUEST, "Validation failed", request.getRequestURI(), fieldErrors);
    }

    @ExceptionHandler(ResponseStatusException.class)
    public ResponseEntity<ErrorResponse> handleResponseStatus(ResponseStatusException exception,
                                                              HttpServletRequest request) {
        HttpStatus status = HttpStatus.valueOf(exception.getStatusCode().value());
        return buildResponse(status, exception.getReason(), request.getRequestURI(), Map.of());
    }

    /**
     * Catches RoleNotFoundException when required roles are missing from the DB.
     * Returns a clean 500 JSON with a helpful message pointing to README seeding instructions.
     * Scoped to RoleNotFoundException only — does not swallow NullPointerException,
     * IllegalStateException, DataAccessException, or other RuntimeExceptions.
     */
    @ExceptionHandler(RoleNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleRoleNotFound(RoleNotFoundException exception,
                                                            HttpServletRequest request) {
        logger.error("Role not found at {}: {}", request.getRequestURI(), exception.getMessage());
        return buildResponse(
                HttpStatus.INTERNAL_SERVER_ERROR,
                exception.getMessage(),
                request.getRequestURI(),
                Map.of()
        );
    }

    private ResponseEntity<ErrorResponse> buildResponse(HttpStatus status,
                                                        String message,
                                                        String path,
                                                        Map<String, String> fieldErrors) {
        ErrorResponse errorResponse = new ErrorResponse(
                status.value(),
                status.getReasonPhrase(),
                message,
                path,
                fieldErrors);
        return ResponseEntity.status(status).body(errorResponse);
    }
}
