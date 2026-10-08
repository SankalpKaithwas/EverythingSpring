package com.example.springBoot.jpa.h2.controller;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.springBoot.jpa.h2.dto.ProductRequest;
import com.example.springBoot.jpa.h2.dto.ProductResponse;
import com.example.springBoot.jpa.h2.model.ApiResponse;
import com.example.springBoot.jpa.h2.service.ProductService;

import jakarta.validation.Valid;

@RestController // Equivalent to @Controller + @ResponseBody on every method
@RequestMapping(path="/api/products", produces = { MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE})
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
	public ResponseEntity<ApiResponse<List<ProductResponse>>> getAll() {
		List<ProductResponse> allProducts = productService.getAllProducts();
		return ResponseEntity.ok(ApiResponse.success("Products fetched successfully", allProducts));
	}

	// 2. GET BY ID: http://localhost:8080/api/products/1
//	@GetMapping("/{id}")
//	public ResponseEntity<Product> getById(@PathVariable Long id) {
//		return productService.getProductById(id).map(product -> ResponseEntity.ok(product))
//				.orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND).build());
//	}
	
	
	/** Rule of Thumb: If the client passes Accept: application/json, Spring responds with JSON. 
	 * If the client passes Accept: application/xml, Spring responds with XML. If no Accept header is specified, Spring defaults to JSON.
	 * Strictly expects XML in request, strictly sends XML in response,
		If you leave @PostMapping without consumes/produces, the endpoint becomes 
		format-agnostic: it accepts both JSON and XML based on the client's headers.
	 */
		
	// 3. POST (Create): http://localhost:8080/api/products
	@PostMapping(consumes = { MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE })
	public ResponseEntity<ApiResponse<ProductResponse>> createProduct(@Valid @RequestBody ProductRequest product) {
		ProductResponse saved = productService.addProduct(product);
		return ResponseEntity.status(HttpStatus.CREATED).
				body(ApiResponse.success("Product Created Succesfully", saved));
	}

	// 4. PUT (Update): http://localhost:8080/api/products/1
//	@PutMapping("/{id}")
//	public ResponseEntity<Product> updateProduct(@PathVariable Long id, @RequestBody Product product) {
//		return productService.updateProduct(id, product).map(updated -> ResponseEntity.ok(updated))
//				.orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND).build());
//	}

	// 5. DELETE: http://localhost:8080/api/products/1
//	@DeleteMapping("/{id}")
//	public ResponseEntity<Void> deleteProduct(@PathVariable Long id) {
//		boolean deleted = productService.deleteProduct(id);
//		if (deleted) {
//			return ResponseEntity.noContent().build(); // HTTP 204 No Content
//		}
//		return ResponseEntity.status(HttpStatus.NOT_FOUND).build(); // HTTP 404
//	}

	
	// After Exception Handling ----->>>
	
	@GetMapping("/search/")
	public ResponseEntity<ApiResponse<ProductResponse>> getByName(@PathVariable Long id) {
		ProductResponse product = productService.getProductById(id);
		return ResponseEntity.ok(ApiResponse.success("Products fetched successfully", product));
	}
	
	// GET by ID : http://localhost:8080/api/products/1
	@GetMapping("/{id}")
	public ResponseEntity<ApiResponse<ProductResponse>> getById(@PathVariable Long id) {
		ProductResponse product = productService.getProductById(id);
		return ResponseEntity.ok(ApiResponse.success("Products fetched successfully", product));
	}
	
	// PUT (Update): http://localhost:8080/api/products/1
	@PutMapping(path = "/{id}", 
	        consumes = { MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE })
	public ResponseEntity<ApiResponse<ProductResponse>> updateProduct(@PathVariable Long id, @Valid @RequestBody ProductRequest productRequest) {
		ProductResponse updated = productService.updateProduct(id, productRequest);
		return ResponseEntity.ok(ApiResponse.success("Updated Successfully", updated));
	}

	// DELETE : http://localhost:8080/api/products/1
	@DeleteMapping("/{id}")
	public ResponseEntity<ApiResponse<Void>> deleteProduct(@PathVariable Long id) {
		productService.deleteProduct(id);
		return ResponseEntity.ok(ApiResponse.success("Product Deleted Successfully",null));
	}
	
	/** Adding CUSTOM Query Endpoints - */
	
	// Search endpoint: http://localhost:8080/api/products/search?keyword=mouse
    @GetMapping("/search")
    public ResponseEntity<ApiResponse<List<ProductResponse>>> searchProducts(@RequestParam String keyword) {
       List<ProductResponse> product =  productService.searchByNameIgnoreCase(keyword);
       return ResponseEntity.ok(ApiResponse.success(String.format("Found Product(s) matching '%s' - ", keyword), product));
    }
	
    // Filter by max price: http://localhost:8080/api/products/filter?maxPrice=50.0
    @GetMapping("/filter")
    public ResponseEntity<ApiResponse<List<ProductResponse>>> filterByMaxPrice(@RequestParam double maxPrice) {
         List<ProductResponse> filterByMaxPrice = productService.filterByMaxPrice(maxPrice);
         return ResponseEntity.ok(ApiResponse.success("List of products within 'Max Price' Range",filterByMaxPrice));
    }

    // Price range filter: http://localhost:8080/api/products/range?min=20&max=100
    @GetMapping("/range")
    public ResponseEntity<ApiResponse<List<ProductResponse>>> filterByRange(@RequestParam double min, @RequestParam double max) {
        return ResponseEntity.ok(ApiResponse.success("Fetched products within the limit " + min +" and " + max + " - ",
        		productService.filterByPriceRange(min, max)));
    }
	
	/** Spring MVC has built-in support for Pageable. When you include Pageable pageable as a method argument,
	 *  Spring automatically parses ?page=, ?size=, and ?sort= from the request URL. 
	 *  @PageableDefault: Sets the defaults if the client doesn't pass query parameters.
		Zero-indexed: In Spring Data, pages start at index 0 (Page 0 = first page).*/
    
	// URL: http://localhost:8080/api/products/paged?page=0&size=3&sort=price,desc
	@GetMapping("/paged")
	public ResponseEntity<ApiResponse<Page<ProductResponse>>> getProductsPaged(
	    @PageableDefault(page = 0, size = 5, sort = "id") Pageable pageable) {
	    return ResponseEntity.ok(ApiResponse.success(productService.getProductsPaginated(pageable)))   ;
	}
	
}
