package com.example.springBoot.jpa.h2.exceptions;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {
	
	/** @RestControllerAdvice: Acts as an interceptor around all controllers. When any controller throws an exception,
	 * it gets caught here instead of crashing or leaking stack traces.
	 * @ExceptionHandler: Maps a specific Java exception class to a handling method.*/
	
	private static final Logger log = LoggerFactory.getLogger(GlobalExceptionHandler.class);
	
	// Catches our custom ResourceNotFoundException
	@ExceptionHandler(ResourceNotFoundException.class)
	public ResponseEntity<ErrorResponse<String>> handleResourceNotFound(ResourceNotFoundException ex) {
		log.warn("ResourceNotFoundException handled: {}", ex.getMessage());
		ErrorResponse<String> error = new ErrorResponse<>(HttpStatus.NOT_FOUND.value(), // 404
				HttpStatus.NOT_FOUND.getReasonPhrase(), ex.getMessage(), LocalDateTime.now());
		return new ResponseEntity<>(error, HttpStatus.NOT_FOUND);
	}

	// Generic fallback for any unhandled exceptions (e.g., NullPointer, Database
	// errors)
	@ExceptionHandler(Exception.class)
	public ResponseEntity<ErrorResponse<String>> handleGeneralException(Exception ex) {
		log.error("Unhandled server exception caught: ", ex.getMessage(), ex);
		ErrorResponse<String> error = new ErrorResponse<>(HttpStatus.INTERNAL_SERVER_ERROR.value(), // 500
				HttpStatus.INTERNAL_SERVER_ERROR.getReasonPhrase(), "An unexpected error occurred: " + ex.getMessage(),
				LocalDateTime.now());
		return new ResponseEntity<>(error, HttpStatus.INTERNAL_SERVER_ERROR);
	}

	// Handles validation / bad input errors (400 Bad Request)
	@ExceptionHandler(IllegalArgumentException.class)
	public ResponseEntity<ErrorResponse<String>> handleBadRequest(IllegalArgumentException ex) {
		log.error("IllegalArgumentException handled: ", ex.getMessage(), ex);
		ErrorResponse<String> error = new ErrorResponse<>(HttpStatus.BAD_REQUEST.value(),
				HttpStatus.BAD_REQUEST.getReasonPhrase(), ex.getMessage(), LocalDateTime.now());
		return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
	}
	
	// If you don't want two products with the exact same name, create a custom conflict exception:
	@ExceptionHandler(ResourceConflictException.class)
	public ResponseEntity<ErrorResponse<String>> handleConflict(ResourceConflictException ex) {
		log.error("ResourceConflictException handled: ", ex.getMessage(), ex);
	    ErrorResponse<String> error = new ErrorResponse<>(
	        HttpStatus.CONFLICT.value(), // 409
	        HttpStatus.CONFLICT.getReasonPhrase(),
	        ex.getMessage(),
	        LocalDateTime.now()
	    );
	    return new ResponseEntity<>(error, HttpStatus.CONFLICT);
	}
	
	@ExceptionHandler(MethodArgumentNotValidException.class)
	public ResponseEntity<ErrorResponse<Map<String, List<String>>>> handleValidationExceptions(MethodArgumentNotValidException ex) {
		log.error("MethodArgumentNotValidException handled: ", ex.getMessage(), ex);
		Map<String, List<String>> fieldErrors = new HashMap<>();

//	    ex.getBindingResult().getFieldErrors().forEach(error -> 
//	        fieldErrors.put(error.getField(), error.getDefaultMessage())
//	    );

	    // Adding all the error messages related to "input" field (Any field).
		ex.getBindingResult().getFieldErrors().forEach(error -> {
			fieldErrors.computeIfAbsent(error.getField().substring(0, 1).toUpperCase() + 
					error.getField().substring(1), k -> new ArrayList<>())
			.add(error.getDefaultMessage());
		});
	    
		ErrorResponse<Map<String, List<String>>> error = new ErrorResponse<>(
		        HttpStatus.BAD_REQUEST.value(),
		        "Validation failed",
		        fieldErrors,
		        LocalDateTime.now()
		    );
	    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
	}
	
	@ExceptionHandler(HttpMessageNotReadableException.class)
	public ResponseEntity<ErrorResponse<String>> handleUnreadable(HttpMessageNotReadableException ex) {
		log.error("HttpMessageNotReadableException handled: ", ex.getMessage(), ex);
		ErrorResponse<String> error = new ErrorResponse<>(
				HttpStatus.BAD_REQUEST.value(),
				HttpStatus.BAD_REQUEST.getReasonPhrase(),
				"Malformed request body",
				LocalDateTime.now());
		return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
	}

}
