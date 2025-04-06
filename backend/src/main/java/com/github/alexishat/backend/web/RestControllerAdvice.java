package com.github.alexishat.backend.web;

import com.github.alexishat.backend.exceptions.AppException;
import com.github.alexishat.backend.exceptions.SessionModusException;
import com.github.alexishat.backend.exceptions.SessionValidException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@ControllerAdvice
public class RestControllerAdvice {

    @ExceptionHandler(AppException.class)
    public ResponseEntity<String> handleAppException(AppException ex) {
        return new ResponseEntity<>(ex.getMessage(), ex.getStatus());
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<String> handleGeneralException(Exception ex) {
        return new ResponseEntity<>("Ein unerwarteter Fehler ist aufgetreten." + ex.getMessage() +ex.getCause(), HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(SessionModusException.class)
    public ResponseEntity<String> handleSessionModusException(SessionModusException ex) {
        return new ResponseEntity<>(ex.getMessage(), HttpStatus.CONFLICT);
    }

    @ExceptionHandler(SessionValidException.class)
    public ResponseEntity<Map<String, List<String>>> handleFormValidationException(SessionValidException ex) {
        List<String> errors = ex.getBindingResult()
                .getFieldErrors()
                .stream()
                .map(error -> error.getField() + ": " + error.getDefaultMessage())
                .collect(Collectors.toList());

        Map<String, List<String>> response = new HashMap<>();
        response.put("validationErrors", errors);

        return ResponseEntity.badRequest().body(response);
    }

}