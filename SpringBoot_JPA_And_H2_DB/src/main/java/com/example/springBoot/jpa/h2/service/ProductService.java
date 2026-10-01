package com.example.springBoot.jpa.h2.service;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.example.springBoot.jpa.h2.model.Product;
import com.example.springBoot.jpa.h2.repository.ProductRepository;

@Service
public class ProductService {

	private final ProductRepository productRepository;

	// Constructor Injection
	public ProductService(ProductRepository productRepository) {
		this.productRepository = productRepository;
	}

	// CREATE / INSERT
	public Product addProduct(Product product) {
		return productRepository.save(product);
	}

	// READ ALL
	public List<Product> getAllProducts() {
		return productRepository.findAll();
	}

	// READ BY ID
	public Optional<Product> getProductById(Long id) {
		return productRepository.findById(id);
	}

	// UPDATE
	public Optional<Product> updateProduct(Long id, Product updatedProduct) {
		return productRepository.findById(id).map(existingProduct -> {
			existingProduct.setName(updatedProduct.getName());
			existingProduct.setPrice(updatedProduct.getPrice());
			return productRepository.save(existingProduct); // save() performs an UPDATE if ID exists
		});
	}

	// DELETE
	public boolean deleteProduct(Long id) {
		if (productRepository.existsById(id)) {
			productRepository.deleteById(id);
			return true;
		}
		return false;
	}

	/**
	 * In Spring Data JPA, save() acts as both INSERT and UPDATE:
	 * 
	 * If the entity has no id (or id is null/0), it executes INSERT.
	 * 
	 * If the entity has an existing id, Hibernate executes UPDATE.
	 */
	
	// Using Custom querying using JPA keywords defined in ProductRepository
	// along with user defined query :-
	
	// Search by keyword ignoring cases
    public List<Product> searchByNameIgnoreCase(String keyword) {
        return productRepository.findByNameContainingIgnoreCase(keyword);
    }

    // Filter by max price
    public List<Product> filterByMaxPrice(double maxPrice) {
        return productRepository.findByPriceLessThanEqual(maxPrice);
    }

    // Filter by price range
    public List<Product> filterByPriceRange(double min, double max) {
        return productRepository.findProductsInPriceRange(min, max);
    }
}
