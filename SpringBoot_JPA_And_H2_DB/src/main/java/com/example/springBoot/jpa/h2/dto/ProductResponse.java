package com.example.springBoot.jpa.h2.dto;

import com.example.springBoot.jpa.h2.model.Product;

public record ProductResponse(Long id, String name, Double price) {
	
	public static ProductResponse fromEntity(Product product) {
		return new ProductResponse(product.getId(), product.getName(), product.getPrice());
	}
}
