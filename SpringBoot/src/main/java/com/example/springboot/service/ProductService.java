package com.example.springboot.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicLong;

import org.springframework.stereotype.Service;

import com.example.springboot.model.Product;

/**
 * @Service registers this class in Spring’s Application Context as a Bean so it
 *          can be injected wherever needed.
 */
@Service
public class ProductService {

	private List<Product> products = new ArrayList<>();
	private AtomicLong counter = new AtomicLong(1);

	public ProductService() {
		// Seed initial sample data
		products.add(new Product(counter.getAndIncrement(), "Mechanical Keyboard", 89.99));
		products.add(new Product(counter.getAndIncrement(), "Wireless Mouse", 49.50));
	}

	public List<Product> getAllProducts() {
		return products;
	}

	public Optional<Product> getProductById(Long id) {
		return products.stream().filter(p -> p.getId().equals(id)).findFirst();
	}

	public Product addProduct(Product product) {
		product.setId(counter.getAndIncrement());
		products.add(product);
		return product;
	}

}
