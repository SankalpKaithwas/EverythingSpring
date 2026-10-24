package com.example.springBoot.jpa.h2.service;

import java.math.BigDecimal;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.springBoot.jpa.h2.dto.ProductPatchRequest;
import com.example.springBoot.jpa.h2.dto.ProductRequest;
import com.example.springBoot.jpa.h2.dto.ProductResponse;
import com.example.springBoot.jpa.h2.exceptions.BadRequestException;
import com.example.springBoot.jpa.h2.exceptions.ResourceConflictException;
import com.example.springBoot.jpa.h2.exceptions.ResourceNotFoundException;
import com.example.springBoot.jpa.h2.model.Product;
import com.example.springBoot.jpa.h2.repository.ProductRepository;

@Service
@Transactional
public class ProductService {

	// Define logger instance for this class
    private static final Logger log = LoggerFactory.getLogger(ProductService.class);
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
	@Transactional(readOnly = true)
	public List<ProductResponse> getAllProducts() {
		return productRepository.findAll().stream().map(ProductResponse::fromEntity).toList();
	}
	/**Breaking Down .stream().map(ProductResponse::fromEntity).toList()
		This single chain transforms a List<Product> into a List<ProductResponse>
	 * List<Product>  ──.stream()──>  Stream<Product>  ──.map(...)──>  Stream<ProductResponse>  ──.toList()──>  List<ProductResponse>
	 * The .map() function takes each incoming item from the stream, transforms it using a function you provide, 
	 * and emits the new transformed item down the pipeline.
	 * The syntax ProductResponse::fromEntity is a method reference in Java. 
	 * It is shorthand for this lambda expression:   
	 * 			Java.map(product -> ProductResponse.fromEntity(product))
	 * Whenever an individual Product flows through the pipeline, Java passes it to your static method:
	 * 
			public static ProductResponse fromEntity(Product product) {
			    return new ProductResponse(product.getId(), product.getName(), product.getPrice());
			}
		The stream shifts from a Stream<Product> into a Stream<ProductResponse>.
			*/

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
	@Transactional(readOnly = true)
    public ProductResponse getProductById(Long id) {
		log.debug("Fetching product details for id={}", id);
        Product product = productRepository.findById(id)
                .orElseThrow(() -> { 
                	log.warn("Lookup failed: Product id={} does not exist", id);
                	return new ResourceNotFoundException("Product not found with id: " + id);
                	});                
        
        return ProductResponse.fromEntity(product);
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
	public ProductResponse updateProduct(Long id, ProductRequest productRequest) {
		Product existing = productRepository.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException("Product not found with id:" + id));
		String trimmedName = productRequest.name().trim();

		boolean nameTakenByAnotherProduct = productRepository.existsByNameIgnoreCaseAndIdNot(trimmedName, id);
		if (nameTakenByAnotherProduct) {
			throw new ResourceConflictException("Product already exists with same name: " + trimmedName);
		}

		existing.setName(trimmedName);
		existing.setPrice(productRequest.price());
		Product updatedProduct = productRepository.save(existing);
		return ProductResponse.fromEntity(updatedProduct);
	}

    
 // THROW EXCEPTION IF DELETING NON-EXISTENT PRODUCT
    public void deleteProduct(Long id) {
    	log.info("Attempting deletion of product id={}", id);
        if (!productRepository.existsById(id)) {
        	log.warn("Delete aborted: Product id={} does not exist", id);
            throw new ResourceNotFoundException("Cannot delete: Product not found with id: " + id);
        }
        productRepository.deleteById(id);
        log.info("Successfully deleted product id={}", id);
    }
    
 // THROW EXCEPTION IF ADDING AN EXISTENT PRODUCT
 // CREATE/INSERT
    public ProductResponse addProduct(ProductRequest productRequest) {
    	String trimmedName = productRequest.name().trim();
    	log.info("Creating new product with name='{}' and price={}", trimmedName, productRequest.price());
        if (productRepository.existsByNameEqualsIgnoreCase(trimmedName)) {
            throw new ResourceConflictException("A product with name '" + trimmedName + "' already exists.");
        }
        Product product = new Product(trimmedName, productRequest.price());
        Product savedProduct = productRepository.save(product);
        log.info("Product created successfully with assigned id={}", savedProduct.getId());
        return ProductResponse.fromEntity(savedProduct);
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
    public List<ProductResponse> searchByNameIgnoreCase(String productName) {
         List<ProductResponse> byNameContainingIgnoreCase = 
        		 productRepository.findByNameContainingIgnoreCase(productName)
        		 .stream().map(ProductResponse:: fromEntity).toList();
         return byNameContainingIgnoreCase ;        		 
    }

    // Filter by max price
    public List<ProductResponse> filterByMaxPrice(BigDecimal maxPrice) {
        return productRepository.findByPriceLessThanEqual(maxPrice).stream().map(ProductResponse::fromEntity).toList();
    }

    // Filter by price range
    public List<ProductResponse> filterByPriceRange(BigDecimal min, BigDecimal max) {
    	if(max.compareTo(min) < 0) {
    		throw new BadRequestException("Min price must not be greater than Max price");
    	}
        return productRepository.findProductsInPriceRange(min, max).stream().map(ProductResponse::fromEntity).toList();
    }
    
    // Pagination
    @Transactional(readOnly = true)
    public Page<ProductResponse> getProductsPaginated(Pageable pageable) {
        return productRepository.findAll(pageable).map(product-> ProductResponse.fromEntity(product));
//        return productRepository.findAll().stream().map(ProductResponse::fromEntity).toList();
        // Both return statements are correct and can be used interchangeably.
    }
    
    public ProductResponse patchProduct(Long id, ProductPatchRequest patchRequest) {
    	log.info("Applying partial update on product id={}", id);
		Product existingProduct = productRepository.findById(id)
				.orElseThrow(() -> {
			log.debug("Patch failed: Product id={} not found", id);
			return new ResourceNotFoundException("Product not found with id: " + id);
		});
		
		if (patchRequest.productName() == null && patchRequest.price() == null) {
			log.debug("Patch rejected: No name or price supplied for product id={}", id);
			throw new BadRequestException("At least one of ‘name’ or ‘price’ must be provided");
		}

        // Update name only if supplied and not purely blank whitespace
        if (patchRequest.productName() != null) {
            String trimmedName = patchRequest.productName().trim();
            if (trimmedName.isEmpty()) {
            	log.debug("Patch rejected: Empty name string supplied for product id={}", id);
                throw new BadRequestException("Product name cannot be empty or blank when provided");
            }
            
            if(productRepository.existsByNameIgnoreCaseAndIdNot(trimmedName, id)) {
            	log.warn("Patch rejected: Product with Same Name Exists. Cannot Patch for product id={}", id);
            	throw new ResourceConflictException("Product already exists with name: " + trimmedName);
            }
            log.debug("Updating name for id={} from '{}' to '{}'", id, existingProduct.getName(), trimmedName);
            existingProduct.setName(trimmedName);
        }

        // Update price only if supplied
        if (patchRequest.price() != null) {
        	log.debug("Updating price for id={} from {} to {}", id, existingProduct.getPrice(), patchRequest.price());
            existingProduct.setPrice(patchRequest.price());
        }

        Product updatedProduct = productRepository.save(existingProduct);
        log.info("Successfully patched product id={}", updatedProduct.getId());
        return ProductResponse.fromEntity(updatedProduct);
    }
    
}
