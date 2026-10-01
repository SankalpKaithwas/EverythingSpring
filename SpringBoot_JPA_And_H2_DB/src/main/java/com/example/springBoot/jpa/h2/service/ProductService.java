package com.example.springBoot.jpa.h2.service;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;

import com.example.springBoot.jpa.h2.exceptions.ResourceConflictException;
import com.example.springBoot.jpa.h2.exceptions.ResourceNotFoundException;
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
//	public Product addProduct(Product product) {
//		return productRepository.save(product);
//	}

	// READ ALL
	public List<Product> getAllProducts() {
		return productRepository.findAll();
	}

	// READ BY ID
//	public Optional<Product> getProductById(Long id) {
//		return productRepository.findById(id);
//	}

	// UPDATE
//	public Optional<Product> updateProduct(Long id, Product updatedProduct) {
//		return productRepository.findById(id).map(existingProduct -> {
//			existingProduct.setName(updatedProduct.getName());
//			existingProduct.setPrice(updatedProduct.getPrice());
//			return productRepository.save(existingProduct); // save() performs an UPDATE if ID exists
//		});
//	}
	
	/** Using Optional - 
	 * What it means: The service says, "Here is a box that might contain a Product, or might be empty. 
	 * You figure out what to do if it's empty."
	 * If found, it wraps the product (Optional.of(product)).
	 * If not found in the database, it returns an empty Optional (Optional.empty()).
	 * 
	 * The consequence: Every controller method has to unpack the Optional manually using .map() and .orElseGet(): */	
	/** So If you have 10 different controller methods fetching a product, you have to duplicate that same Optional checking
	 *  and null-handling logic in every single one.
	 *  
	 *  TO RESOLVE THIS:-- "Returning Product and Throwing an EXCEPTIONS" ------>>>>>>>>*/
	
	// THROW EXCEPTION IF NOT FOUND
    public Product getProductById(Long id) {
        return productRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Product not found with id: " + id));
    }
    
    /**BENEFIT OF ABOVE - 
     * What it means: The service guarantees: "If this method completes, you will 100% receive a valid Product. If the 
     * record does not exist in the database, execution stops immediately and throws ResourceNotFoundException."
	 * The consequence: The controller no longer cares about error handling, branching, or HTTP 404 logic in ProductController as - 
	 * 	@PutMapping("/{id}")
	public ResponseEntity<Product> updateProduct(@PathVariable Long id, @RequestBody Product product) {
		Product updated = productService.updateProduct(id, product);
		return ResponseEntity.ok(updated);
	}
     */
    
 // THROW EXCEPTION IF UPDATING NON-EXISTENT PRODUCT
    public Product updateProduct(Long id, Product updatedProduct) {
        Product existing = getProductById(id); // reuses the check above
        existing.setName(updatedProduct.getName());
        existing.setPrice(updatedProduct.getPrice());
        return productRepository.save(existing);
    }
	
	// DELETE
//	public boolean deleteProduct(Long id) {
//		if (productRepository.existsById(id)) {
//			productRepository.deleteById(id);
//			return true;
//		}
//		return false;
//	}
    
 // THROW EXCEPTION IF DELETING NON-EXISTENT PRODUCT
    public void deleteProduct(Long id) {
        if (!productRepository.existsById(id)) {
            throw new ResourceNotFoundException("Cannot delete: Product not found with id: " + id);
        }
        productRepository.deleteById(id);
    }
    
 // THROW EXCEPTION IF ADDING AN EXISTENT PRODUCT
 // CREATE/INSERT
    public Product addProduct(Product product) {
        if (!productRepository.findByNameContainingIgnoreCase(product.getName()).isEmpty()) {
            throw new ResourceConflictException("A product with name '" + product.getName() + "' already exists.");
        }
        return productRepository.save(product);
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
    
    // Pagination
    public Page<Product> getProductsPaginated(Pageable pageable) {
        return productRepository.findAll(pageable);
    }
}
