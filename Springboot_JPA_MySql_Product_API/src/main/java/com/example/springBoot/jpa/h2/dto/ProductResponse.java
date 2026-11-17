package com.example.springBoot.jpa.h2.dto;

import java.math.BigDecimal;

import com.example.springBoot.jpa.h2.model.Product;

public record ProductResponse(Long id, String name, BigDecimal price) {
	
	public static ProductResponse fromEntity(Product product) {
		return new ProductResponse(product.getId(), product.getName(), product.getPrice());
	}
	
	/** The fromEntity method is a common static utility pattern in Java used to convert a database Entity 
	 * into a DTO (Data Transfer Object) or response object.  It typically resides within the DTO class 
	 * and accepts an entity instance as a parameter, mapping its fields to the DTO. */
}
