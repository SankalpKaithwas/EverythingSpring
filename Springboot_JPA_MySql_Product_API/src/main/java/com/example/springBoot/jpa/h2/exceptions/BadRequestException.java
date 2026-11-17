package com.example.springBoot.jpa.h2.exceptions;

public class BadRequestException extends RuntimeException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	public BadRequestException(String message) {
		super(message);
	}
}
