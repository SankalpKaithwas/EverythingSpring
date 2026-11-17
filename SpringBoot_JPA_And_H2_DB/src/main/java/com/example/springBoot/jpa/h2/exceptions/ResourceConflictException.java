package com.example.springBoot.jpa.h2.exceptions;

public class ResourceConflictException extends RuntimeException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public ResourceConflictException(String message) {
		super(message);
	}
}
