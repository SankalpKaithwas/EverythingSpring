package com.example.springBoot.jpa.h2.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;

public record ProductRequest(
		@NotBlank(message = "Product name is required and cannot be blank") 
		@Size(min = 2, max = 100, message = "Product name must be between 2 and 100 characters") 
		String name,

		@NotNull(message = "Price is required") @Positive(message = "Price must be greater than zero") 
		Double price) {
	
	public ProductRequest {                 // no ( ) after the name
        if (name != null) {
            name = name.trim();    // reassign the parameter, not a field
        }
    }
	
	/** Using compact constructor. The order of events for a request is:
		Jackson builds the record through its constructor, and your trim runs.
		@Valid validates the finished object, so @Size(min = 2) now sees "a" and rejects it.
		The controller and service receive an already-trimmed name. */
}
