package com.bahmet.urlmonitor.controller;

import com.bahmet.urlmonitor.dto.ErrorDetails;
import com.bahmet.urlmonitor.exception.MonitoredEndpointNotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.validation.FieldError;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.resource.NoResourceFoundException;

import java.util.Date;

@ControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    @ExceptionHandler({MonitoredEndpointNotFoundException.class, HttpRequestMethodNotSupportedException.class, NoResourceFoundException.class})
    public ResponseEntity<?> handleNotFoundException(Exception ex) {
        log.error("Exception occurred", ex);

        ErrorDetails errorDetails = new ErrorDetails(new Date(), ex.getMessage());

        return new ResponseEntity<>(errorDetails, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler({DataIntegrityViolationException.class, IllegalArgumentException.class})
    public ResponseEntity<?> handleDataIntegrityViolationException(RuntimeException ex) {
        log.error("Exception occurred", ex);

        ErrorDetails errorDetails = new ErrorDetails(new Date(), ex.getMessage());

        return new ResponseEntity<>(errorDetails, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<?> handleValidationException(MethodArgumentNotValidException ex) {
        log.error("Exception occurred", ex);

        String message = ex.getBindingResult().getFieldErrors().stream()
                .findFirst()
                .map(FieldError::getDefaultMessage)
                .orElse("Validation error");

        ErrorDetails errorDetails = new ErrorDetails(new Date(), message);

        return new ResponseEntity<>(errorDetails, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(BadCredentialsException.class)
    public ResponseEntity<?> handleBadCredentialsException(Exception ex) {
        log.error("Exception occurred", ex);

        ErrorDetails errorDetails = new ErrorDetails(new Date(), ex.getMessage());

        return new ResponseEntity<>(errorDetails, HttpStatus.UNAUTHORIZED);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<?> handleGlobalException(Exception ex) {
        log.error("Exception occurred", ex);

        ErrorDetails errorDetails = new ErrorDetails(new Date(), ex.getMessage());

        return new ResponseEntity<>(errorDetails, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
