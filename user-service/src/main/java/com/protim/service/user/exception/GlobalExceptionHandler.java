package com.protim.service.user.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import tools.jackson.databind.exc.InvalidFormatException;

import java.time.format.DateTimeParseException;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<ErrorResponse> handleJsonParseError(HttpMessageNotReadableException ex) {
        log.error("HttpMessageNotReadableException in user-service", ex);
        String errorMessage = "Invalid request body format.";

        // Check if the root cause is a bad date-time string
        if (ex.getCause() instanceof InvalidFormatException invalidFormatException) {
            if (invalidFormatException.getCause() instanceof DateTimeParseException) {
                errorMessage = "Invalid date provided or incorrect format. Please use dd-mm-yyyy (e.g., 28-02-1993).";
            }
        }

        var status = HttpStatus.BAD_REQUEST;
        return ResponseEntity
                .status(status)
                .body(ErrorResponse.builder()
                        .status(status)
                        .message(errorMessage)
                        .build());
    }

    @ExceptionHandler(BadRequestException.class)
    public ResponseEntity<ErrorResponse> handleBadRequestException(BadRequestException ex) {
        var status = HttpStatus.BAD_REQUEST;
        return ResponseEntity
                .status(status)
                .body(ErrorResponse.builder()
                        .status(status)
                        .message(ex.getMessage())
                        .build());
    }

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleResourceNotFoundException(ResourceNotFoundException ex) {
        var status = HttpStatus.NOT_FOUND;
        return ResponseEntity
                .status(status)
                .body(ErrorResponse.builder()
                        .status(status)
                        .message(ex.getMessage())
                        .build());
    }
}
