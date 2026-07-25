package com.pagepulse.backend.exception;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.SocketTimeoutException;
import java.util.HashMap;
import java.util.Map;

import org.jsoup.HttpStatusException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(MalformedURLException.class)
    public ResponseEntity<Map<String, String>> handleInvalidUrl() {

        Map<String, String> error = new HashMap<>();
        error.put("error", "Invalid URL");

        return ResponseEntity.badRequest().body(error);
    }

    @ExceptionHandler(SocketTimeoutException.class)
    public ResponseEntity<Map<String, String>> handleTimeout() {

        Map<String, String> error = new HashMap<>();
        error.put("error", "Request timed out");

        return ResponseEntity.status(HttpStatus.REQUEST_TIMEOUT).body(error);
    }

    @ExceptionHandler(HttpStatusException.class)
    public ResponseEntity<Map<String, String>> handleHttp(HttpStatusException ex) {

        Map<String, String> error = new HashMap<>();

        switch (ex.getStatusCode()) {
            case 403 -> error.put("error", "Access denied. This website blocks automated requests.");
            case 404 -> error.put("error", "Website not found.");
            default -> error.put("error", "Unable to fetch the webpage.");
        }

        return ResponseEntity.status(ex.getStatusCode()).body(error);
    }

    @ExceptionHandler(IOException.class)
    public ResponseEntity<Map<String, String>> handleIo(IOException ex) {

        Map<String, String> error = new HashMap<>();
        error.put("error", ex.getMessage());

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, String>> handleValidation() {

        Map<String, String> error = new HashMap<>();
        error.put("error", "URL is required");

        return ResponseEntity.badRequest().body(error);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<Map<String, String>> handleException(Exception ex) {

        Map<String, String> error = new HashMap<>();
        error.put("error", "Something went wrong");

        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
    }
}