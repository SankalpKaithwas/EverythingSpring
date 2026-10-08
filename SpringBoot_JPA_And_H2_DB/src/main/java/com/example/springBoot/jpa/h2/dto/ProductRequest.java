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
}
