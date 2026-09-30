package com.example.springboot.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.springboot.model.Product;
import com.example.springboot.service.ProductService;

@RestController
@RequestMapping("/api/products") // @RequestMapping("/api/products") is Class-level: sets the base URI
public class ProductController {
	
	private final ProductService productService;

    // Constructor Injection (Spring automatically supplies ProductService)
    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    // 1. GET all products: http://localhost:8080/api/products
    @GetMapping
    public List<Product> getAll() {
        return productService.getAllProducts();
    }

    // 2. GET by ID: http://localhost:8080/api/products/1
    @GetMapping("/{id}")
    public ResponseEntity<Product> getById(@PathVariable Long id) {
        return productService.getProductById(id)
                .map(product -> ResponseEntity.ok(product))
                .orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND).build());
    }

    // 3. POST new product (receives JSON body)
    @PostMapping
    public ResponseEntity<Product> createProduct(@RequestBody Product product) {
        Product savedProduct = productService.addProduct(product);
        return ResponseEntity.status(HttpStatus.CREATED).body(savedProduct);
    }
    /**
    @RequestMapping("/api/products"): Sets a common URL prefix for every endpoint in this class.

    @PathVariable: Binds {id} from the URL path directly to a Java parameter.

    @RequestBody: Automatically reads the incoming JSON payload and converts it into a Product object.

    ResponseEntity: Gives you full control over HTTP status codes (200 OK, 201 CREATED, 404 NOT FOUND).
     
    @GetMapping cannot be used at the class level. If you try to place @GetMapping("/api/products") on 
    the class declaration, it will throw a compilation error because its 
    target scope is restricted to methods: @Target(ElementType.METHOD)
    */
    
    /** @RequestMapping Both class level (to set a base URL) and method level.
     * @RequestMapping belongs at the class level to establish a shared base path for
     *  all endpoints in that controller.
     *  If you use @RequestMapping("/products") on a method without specifying 
     *  method = RequestMethod.GET, it will match any incoming HTTP method—GET, POST, PUT, DELETE, etc.
     *  —which is usually not intended and can introduce bugs or security flaws.  */
    
    /** The Full Family of Method AnnotationsSpring 4.3 added specific shortcuts for all standard
     *  HTTP methods:
     *  @GetMapping -> @RequestMapping(method = RequestMethod.GET)
     *  @PostMapping -> @RequestMapping(method = RequestMethod.POST)
     *  @PutMapping -> @RequestMapping(method = RequestMethod.PUT)
     *  @DeleteMapping -> @RequestMapping(method = RequestMethod.DELETE)
     *  @PatchMapping -> @RequestMapping(method = RequestMethod.PATCH)
     *   ### Rule of thumb: Use @RequestMapping("/base-url") at the class level to set your root path, 
     *   and use the specific method variants (@GetMapping, @PostMapping, etc.) on each handler method 
     *   inside the class. */
}
