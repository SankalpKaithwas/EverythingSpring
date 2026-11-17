package com.example.springBoot.jpa.h2.exceptions;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.mapping.PropertyReferenceException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ProblemDetail;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;
import org.springframework.web.servlet.resource.NoResourceFoundException;

@RestControllerAdvice
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler{
	
	/** @RestControllerAdvice: Acts as an interceptor around all controllers. When any controller throws an exception,
	 * it gets caught here instead of crashing or leaking stack traces.
	 * @ExceptionHandler: Maps a specific Java exception class to a handling method.*/
	
	private static final Logger log = LoggerFactory.getLogger(GlobalExceptionHandler.class);
	
	// Catches our custom ResourceNotFoundException
	@ExceptionHandler(ResourceNotFoundException.class)
	public ResponseEntity<ErrorResponse<String>> handleResourceNotFound(ResourceNotFoundException ex) {
		log.warn("ResourceNotFoundException handled: {}", ex.getMessage(), ex);
		ErrorResponse<String> error = new ErrorResponse<>(HttpStatus.NOT_FOUND.value(), // 404
				HttpStatus.NOT_FOUND.getReasonPhrase(), ex.getMessage(), LocalDateTime.now());
		return new ResponseEntity<>(error, HttpStatus.NOT_FOUND);
	}

	// Generic fallback for any unhandled exceptions (e.g., NullPointer, Database
	// errors)
	@ExceptionHandler(Exception.class)
	public ResponseEntity<ErrorResponse<String>> handleGeneralException(Exception ex, WebRequest request) {
		log.error("Unhandled server exception caught: [{}]", request.getDescription(false), ex );
		ErrorResponse<String> error = new ErrorResponse<>(HttpStatus.INTERNAL_SERVER_ERROR.value(), // 500
				HttpStatus.INTERNAL_SERVER_ERROR.getReasonPhrase(), 
				"An unexpected error occurred. Please try again later",
				LocalDateTime.now());
		return new ResponseEntity<>(error, HttpStatus.INTERNAL_SERVER_ERROR);
	}

	// Handles validation / bad input errors (400 Bad Request)
	@ExceptionHandler(IllegalArgumentException.class)
	public ResponseEntity<ErrorResponse<String>> handleBadRequest(IllegalArgumentException ex) {
		log.warn("IllegalArgumentException handled: {}", ex.getMessage(), ex);
		ErrorResponse<String> error = new ErrorResponse<>(HttpStatus.BAD_REQUEST.value(),
				HttpStatus.BAD_REQUEST.getReasonPhrase(), ex.getMessage(), LocalDateTime.now());
		return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
	}
	
	// If you don't want two products with the exact same name, create a custom conflict exception:
	@ExceptionHandler(ResourceConflictException.class)
	public ResponseEntity<ErrorResponse<String>> handleConflict(ResourceConflictException ex) {
		log.warn("ResourceConflictException handled: {}", ex.getMessage(), ex);
	    ErrorResponse<String> error = new ErrorResponse<>(
	        HttpStatus.CONFLICT.value(), // 409
	        HttpStatus.CONFLICT.getReasonPhrase(),
	        ex.getMessage(),
	        LocalDateTime.now()
	    );
	    return new ResponseEntity<>(error, HttpStatus.CONFLICT);
	}
	
	@Override
	protected ResponseEntity<Object> handleMethodArgumentNotValid(
			MethodArgumentNotValidException ex, HttpHeaders headers, HttpStatusCode status, WebRequest request) {
		
		log.warn("MethodArgumentNotValidException handled: {}", ex.getMessage(), ex);
		Map<String, List<String>> fieldErrors = new HashMap<>();

	    // Adding all the error messages related to "input" field (Any field).
		ex.getBindingResult().getFieldErrors().forEach(error -> {
			fieldErrors.computeIfAbsent(error.getField().substring(0, 1).toUpperCase() + 
					error.getField().substring(1), k -> new ArrayList<>())
			.add(error.getDefaultMessage());
		});
	    
		ErrorResponse<Map<String, List<String>>> errorResponse = new ErrorResponse<>(
				status.value(),
		        "Validation failed",
		        fieldErrors,
		        LocalDateTime.now()
		    );
		return handleExceptionInternal(ex, errorResponse, headers, status, request);
	}
	
	@Override
	protected ResponseEntity<Object> handleHttpMessageNotReadable(
			HttpMessageNotReadableException ex, HttpHeaders headers, HttpStatusCode status, WebRequest request) {
		log.warn("HttpMessageNotReadableException handled: {}", ex.getMessage(), ex); 
		ErrorResponse<String> error = new ErrorResponse<>(
				status.value(),
				HttpStatus.BAD_REQUEST.getReasonPhrase(),
				"Malformed request body",
				LocalDateTime.now());
		return handleExceptionInternal(ex, error, headers, status, request);
	}
	
	@Override
	protected ResponseEntity<Object> handleNoResourceFoundException(NoResourceFoundException ex, HttpHeaders headers,
			HttpStatusCode status, WebRequest request) {
		ErrorResponse<String> errorResponse = new ErrorResponse<String>(
				status.value(), "Resource Not Found", ex.getMessage(), LocalDateTime.now());
			
		return handleExceptionInternal(ex, errorResponse, headers, status, request);
	}

	@Override
	protected ResponseEntity<Object> handleExceptionInternal(Exception ex, Object body, HttpHeaders headers,
			HttpStatusCode statusCode, WebRequest request) {
		
		// 1. Already in our envelope (validation, malformed JSON): keep it as is
	    if (body instanceof ErrorResponse) {
	    	log.error("handleExceptionInternal: [{}]", request.getDescription(false), ex);
	        return super.handleExceptionInternal(ex, body, headers, statusCode, request);
	    }
	    
		// 2. Reason phrase, with a fallback for non-standard status codes
		HttpStatus httpStatus = HttpStatus.resolve(statusCode.value());
		String reason = (httpStatus != null) ? httpStatus.getReasonPhrase() : "Error";

		// 3. Find Spring's own detail text, wherever it lives
		ProblemDetail problem = null;
		if (body instanceof ProblemDetail pd) {
			problem = pd; // e.g. type mismatch
		} else if (body == null && ex instanceof org.springframework.web.ErrorResponse springError) {
			problem = springError.getBody(); // e.g. 404, 405, 415, missing param
		}
		String detail = (problem != null) ? problem.getDetail() : null;

		ErrorResponse<String> errorResponse = 
				new ErrorResponse<String>(statusCode.value(), 
						reason,detail, LocalDateTime.now());
		log.error("handleExceptionInternal: {}", ex);
		return super.handleExceptionInternal(ex, errorResponse, headers, statusCode, request);
	}
	
	@ExceptionHandler(PropertyReferenceException.class)
	public ResponseEntity<ErrorResponse<String>> 
			handlePropertyReferenceException(PropertyReferenceException ex, WebRequest request) {
		log.warn("handlePropertyReferenceException: [{}]", request.getDescription(false),
				ex);

		ErrorResponse<String> errorResponse = new ErrorResponse<String>(HttpStatus.BAD_REQUEST.value(),
				HttpStatus.BAD_REQUEST.getReasonPhrase(),"Invalid Sort property: " + ex.getPropertyName(),
				LocalDateTime.now());

		return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
		/** false versus true. getDescription(false) gives only uri=.... 
		 * getDescription(true) adds client details such as the remote address and 
		 * session id, which you don’t want in routine logs. */
	}
}
