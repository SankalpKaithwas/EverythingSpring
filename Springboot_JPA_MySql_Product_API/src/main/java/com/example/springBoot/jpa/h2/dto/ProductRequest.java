package com.example.springBoot.jpa.h2.dto;

import java.math.BigDecimal;

import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;

public record ProductRequest(
		@NotBlank(message = "Product name is required and cannot be blank") 
		@Size(min = 2, max = 100, message = "Product name must be between 2 and 100 characters") 
		String name,

		@NotNull(message = "Price is required") 
		@Positive(message = "Price must be greater than zero") 
		@Digits(integer = 8, fraction = 2, 
				message = "Price must be less than 100000000 and within 2 decimal places")
		BigDecimal price) {
	
	public ProductRequest { // no ( ) after the name
		if (name != null) {
			name = name.trim(); // reassign the parameter, not a field
		}
		if (price != null && price.scale() < 2) {
			price = price.setScale(2);
		}
	}
	
	/** Using compact constructor. The order of events for a request is:
		Jackson builds the record through its constructor, and your trim runs.
		@Valid validates the finished object, so @Size(min = 2) now sees "a" and rejects it.
		The controller and service receive an already-trimmed name. */
	
	/** setScale() sets the limit of decimal place. So 2 means only 2 decimal places but if only 1 digit is 
	 * provided after the decimal, then it will add 0 after the 1st digit. Ex- 89.9 will become 89.90.  */
}
