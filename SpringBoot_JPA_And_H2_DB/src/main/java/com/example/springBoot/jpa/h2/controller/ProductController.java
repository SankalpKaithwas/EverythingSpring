package com.example.springBoot.jpa.h2.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.springBoot.jpa.h2.model.Product;
import com.example.springBoot.jpa.h2.service.ProductService;

@RestController
@RequestMapping("/api/products")
public class ProductController {

	/**
	 * Test Using Insomnia.
	 * A complete RESTful CRUD API supporting GET, POST, PUT and DELETE requests.
	 */

	private final ProductService productService;

	public ProductController(ProductService productService) {
		this.productService = productService;
	}

	// 1. GET ALL: http://localhost:8080/api/products
	@GetMapping
	public List<Product> getAll() {
		return productService.getAllProducts();
	}

	// 2. GET BY ID: http://localhost:8080/api/products/1
	@GetMapping("/{id}")
	public ResponseEntity<Product> getById(@PathVariable Long id) {
		return productService.getProductById(id).map(product -> ResponseEntity.ok(product))
				.orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND).build());
	}

	// 3. POST (Create): http://localhost:8080/api/products
	@PostMapping
	public ResponseEntity<Product> createProduct(@RequestBody Product product) {
		Product saved = productService.addProduct(product);
		return ResponseEntity.status(HttpStatus.CREATED).body(saved);
	}

	// 4. PUT (Update): http://localhost:8080/api/products/1
	@PutMapping("/{id}")
	public ResponseEntity<Product> updateProduct(@PathVariable Long id, @RequestBody Product product) {
		return productService.updateProduct(id, product).map(updated -> ResponseEntity.ok(updated))
				.orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND).build());
	}

	// 5. DELETE: http://localhost:8080/api/products/1
	@DeleteMapping("/{id}")
	public ResponseEntity<Void> deleteProduct(@PathVariable Long id) {
		boolean deleted = productService.deleteProduct(id);
		if (deleted) {
			return ResponseEntity.noContent().build(); // HTTP 204 No Content
		}
		return ResponseEntity.status(HttpStatus.NOT_FOUND).build(); // HTTP 404
	}
}
