package com.example.springBoot.jpa.h2.exceptions;

import java.time.LocalDateTime;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {
	// Catches our custom ResourceNotFoundException
	@ExceptionHandler(ResourceNotFoundException.class)
	public ResponseEntity<ErrorResponse> handleResourceNotFound(ResourceNotFoundException ex) {
		ErrorResponse error = new ErrorResponse(HttpStatus.NOT_FOUND.value(), // 404
				HttpStatus.NOT_FOUND.getReasonPhrase(), ex.getMessage(), LocalDateTime.now());
		return new ResponseEntity<>(error, HttpStatus.NOT_FOUND);
	}

	// Generic fallback for any unhandled exceptions (e.g., NullPointer, Database
	// errors)
	@ExceptionHandler(Exception.class)
	public ResponseEntity<ErrorResponse> handleGeneralException(Exception ex) {
		ErrorResponse error = new ErrorResponse(HttpStatus.INTERNAL_SERVER_ERROR.value(), // 500
				HttpStatus.INTERNAL_SERVER_ERROR.getReasonPhrase(), "An unexpected error occurred: " + ex.getMessage(),
				LocalDateTime.now());
		return new ResponseEntity<>(error, HttpStatus.INTERNAL_SERVER_ERROR);
	}

	// Handles validation / bad input errors (400 Bad Request)
	@ExceptionHandler(IllegalArgumentException.class)
	public ResponseEntity<ErrorResponse> handleBadRequest(IllegalArgumentException ex) {
		ErrorResponse error = new ErrorResponse(HttpStatus.BAD_REQUEST.value(),
				HttpStatus.BAD_REQUEST.getReasonPhrase(), ex.getMessage(), LocalDateTime.now());
		return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
	}
	
	// If you don't want two products with the exact same name, create a custom conflict exception:
	@ExceptionHandler(ResourceConflictException.class)
	public ResponseEntity<ErrorResponse> handleConflict(ResourceConflictException ex) {
	    ErrorResponse error = new ErrorResponse(
	        HttpStatus.CONFLICT.value(), // 409
	        HttpStatus.CONFLICT.getReasonPhrase(),
	        ex.getMessage(),
	        LocalDateTime.now()
	    );
	    return new ResponseEntity<>(error, HttpStatus.CONFLICT);
	}
	
	/** @RestControllerAdvice: Acts as an interceptor around all controllers. When any controller throws an exception,
	 * it gets caught here instead of crashing or leaking stack traces.
	 * @ExceptionHandler: Maps a specific Java exception class to a handling method.*/
}
