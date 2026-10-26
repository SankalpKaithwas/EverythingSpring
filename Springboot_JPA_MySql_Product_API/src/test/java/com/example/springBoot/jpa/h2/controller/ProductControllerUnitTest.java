package com.example.springBoot.jpa.h2.controller;

import static org.assertj.core.api.Assertions.not;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.not;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.List;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mapping.PropertyReferenceException;
import org.springframework.data.util.TypeInformation;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import com.example.springBoot.jpa.h2.config.JacksonConfig;
import com.example.springBoot.jpa.h2.dto.ProductRequest;
import com.example.springBoot.jpa.h2.dto.ProductResponse;
import com.example.springBoot.jpa.h2.exceptions.BadRequestException;
import com.example.springBoot.jpa.h2.exceptions.GlobalExceptionHandler;
import com.example.springBoot.jpa.h2.exceptions.ResourceConflictException;
import com.example.springBoot.jpa.h2.exceptions.ResourceNotFoundException;
import com.example.springBoot.jpa.h2.model.Product;
import com.example.springBoot.jpa.h2.service.ProductService;
import com.fasterxml.jackson.databind.ObjectMapper;

//@WebMvcTest mocks the service and never touches a database.
@WebMvcTest(ProductController.class)
@Import({ JacksonConfig.class, GlobalExceptionHandler.class })
public class ProductControllerUnitTest {
	
	@Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    // Use @MockitoBean (Spring Boot 3.4+) or @MockBean (earlier Boot versions)
    @MockitoBean
    private ProductService productService;
    
    @Test
    @DisplayName("GET /api/products - Should return 200 with PascalCase envelope")
    void shouldReturnAllProductsWithPascalCase() throws Exception {
        List<ProductResponse> mockList = List.of(
            new ProductResponse(1L, "Mechanical Keyboard", new BigDecimal("110.00")),
            new ProductResponse(2L, "Optical Mouse", new BigDecimal("25.00"))
        );

        when(productService.getAllProducts()).thenReturn(mockList);

        ResultActions perform = mockMvc.perform(get("/api/products")
                .accept(MediaType.APPLICATION_JSON));
        
        perform.andExpect(status().isOk())
                .andExpect(jsonPath("$.Success").value(true))
                .andExpect(jsonPath("$.Message").value("Products fetched successfully"))
                .andExpect(jsonPath("$.Data[0].Id").value(1))
                .andExpect(jsonPath("$.Data[0].Name").value("Mechanical Keyboard"))
                .andExpect(jsonPath("$.Data[0].Price").value(110.00));
    }
    
    @Test
    @DisplayName("POST /api/products - Should return 201 when payload is valid")
    void shouldCreateProductSuccessfully() throws Exception {
        ProductRequest validRequest = new ProductRequest("Gaming Headset", new BigDecimal("89.99"));
        ProductResponse savedResponse = new ProductResponse(10L, "Gaming Headset", new BigDecimal("89.99"));

//    	If ProductRequest does not override equals(), use any(ProductRequest.class) instead.
//        (By this way you tell Mockito: "I don't care which exact instance arrives, as long as it is an 
//        object of type ProductRequest.)
        when(productService.addProduct(any(ProductRequest.class))).thenReturn(savedResponse);

        ResultActions result = mockMvc.perform(post("/api/products")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(validRequest))); // Providing the body
        
        result.andExpect(status().isCreated())
                .andExpect(jsonPath("$.Success").value(true))
                .andExpect(jsonPath("$.Data.Id").value(10))
                .andExpect(jsonPath("$.Data.Name").value("Gaming Headset"))
                .andExpect(jsonPath("$.Data.Price").value(89.99));
    }
    
    @Test
    @DisplayName("POST /api/products - Should return 400 when validation fails")
    void shouldReturn400WhenValidationFails() throws Exception {
        // Invalid request: blank name and zero/negative price
        ProductRequest invalidRequest = new ProductRequest("", new BigDecimal("-5.0"));

        mockMvc.perform(post("/api/products")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(invalidRequest)))        
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.StatusCode").value(400))
                .andExpect(jsonPath("$.Errors.Name").exists())
                .andExpect(jsonPath("$.Errors.Price").exists());
    }
    
    @Test
    @DisplayName("GET /api/products/{id} - Should return 404 when resource not found")
    void shouldReturn404WhenNotFound() throws Exception {
        when(productService.getProductById(999L))
            .thenThrow(new ResourceNotFoundException("Product not found with id: 999"));

        mockMvc.perform(get("/api/products/999")
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.StatusCode").value(404))
                .andExpect(jsonPath("$.Message").value("Not Found"))
                .andExpect(jsonPath("$.Errors").value("Product not found with id: 999"));
    }
    
	@Test
    @DisplayName("GET to unmapped endpoint should return 404 with error payload")
    void testInvalidUriReturnsNotFound() throws Exception {
    	
		//No controller or static file matches the URL, such as GET /api/nothing
    	mockMvc.perform(get("/api/products/paged/sort")
    			.accept(MediaType.APPLICATION_JSON))
    			.andExpect(status().isNotFound())
    			.andExpect(jsonPath("$.StatusCode").value(404))
    			.andExpect(jsonPath("$.Message").value("Resource Not Found"))
    			.andExpect(jsonPath("$.Errors").value("No static resource api/products/paged/sort."));
    	
    	mockMvc.perform(get("/api/anything")
    			.accept(MediaType.APPLICATION_JSON))
    			.andExpect(status().isNotFound())
    			.andExpect(jsonPath("$.StatusCode").value(404))
    			.andExpect(jsonPath("$.Message").value("Resource Not Found"))
    			.andExpect(jsonPath("$.Errors").value("No static resource api/anything."));
        
    }
	

    @Test
    @DisplayName("POST /api/products - Should return 409 when Product already exists")
    void shouldReturn409ForResourceConflictException() throws Exception {
    	ProductRequest validRequest = new ProductRequest("Desk Mat", new BigDecimal("89.99"));
    	
    	when(productService.addProduct(validRequest))
        .thenThrow(new ResourceConflictException("A product with name 'Desk Mat' already exists."));
    	
//    	If ProductRequest does not override equals(), use any(ProductRequest.class) instead.
//      (By this way you tell Mockito: "I don't care which exact instance arrives, as long as it is an 
//      object of type ProductRequest. This only applies to classes. records implements equal so need to do it.)
//    	when(productService.addProduct(any(ProductRequest.class)))
//        .thenThrow(new ResourceConflictException("A product with name 'Desk Mat' already exists."));

        mockMvc.perform(post("/api/products")
        		.contentType(MediaType.APPLICATION_JSON)
        		.accept(MediaType.APPLICATION_JSON)
        		.content(objectMapper.writeValueAsString(validRequest))) // Providing the body
        		.andExpect(status().isConflict())
        		.andExpect(jsonPath("$.StatusCode").value(409))
        		.andExpect(jsonPath("$.Message").value("Conflict"))
        		.andExpect(jsonPath("$.Errors").value("A product with name 'Desk Mat' already exists."));
    }
    
    
    
    @Test
    @DisplayName("POST /api/products - Should return 400 when request body not provided")
    void shouldReturn400ForMessageNotReadable() throws Exception {
        
        mockMvc.perform(post("/api/products")
        		.contentType(MediaType.APPLICATION_JSON)
        		.accept(MediaType.APPLICATION_JSON))
        		.andExpect(status().isBadRequest())
        		.andExpect(jsonPath("$.StatusCode").value(400))
        		.andExpect(jsonPath("$.Message").value("Bad Request"))
        		.andExpect(jsonPath("$.Errors").value("Malformed request body"));
    }
    
    @Test
    @DisplayName("POST /api/products - Should return 500 Internal Server Error for RuntimeException")
    void shouldReturn500ForInternalServerError() throws Exception {
    	ProductRequest validRequest = new ProductRequest("Desk Mat", new BigDecimal("89.99"));
    	
        when(productService.addProduct(validRequest))
        .thenThrow(new RuntimeException("Secret"));
        
        ResultActions perform = mockMvc.perform(post("/api/products")
        		.contentType(MediaType.APPLICATION_JSON)
        		.accept(MediaType.APPLICATION_JSON)
        		.content(objectMapper.writeValueAsString(validRequest)));
        perform.andExpect(status().isInternalServerError())
        		.andExpect(jsonPath("$.StatusCode").value(500))
        		.andExpect(jsonPath("$.Message").value("Internal Server Error"))
        		.andExpect(jsonPath("$.Errors").value("An unexpected error occurred. Please try again later"))
        		.andExpect(content().string(not(containsString("Secret"))));
        
        //andExpect(content().string(not(containsString("Secret")))) - this assertion watches for the leak that
        // if response body doesn't contains the error that was not intended for client.
        		
    }
    
    @Test
    @DisplayName("GET /api/products/range - Should return 400 BadRequestException when min price is greater")
    void shouldReturn400ForBadRequestExceptionWhenMinPriceIsGreater() throws Exception {
    	
    	BigDecimal min = new BigDecimal("50");
    			BigDecimal max = new BigDecimal("10");
 
        when(productService.filterByPriceRange(min, max))
        	.thenThrow(new BadRequestException("Min price must not be greater than Max price"));
        
        mockMvc.perform(get("/api/products/range")
        		.contentType(MediaType.APPLICATION_JSON)
        		.accept(MediaType.APPLICATION_JSON)
        		.queryParam("min", min.toString())
        		.queryParam("max", max.toString()))
        		.andExpect(status().isBadRequest())
        		.andExpect(jsonPath("$.StatusCode").value(400))
        		.andExpect(jsonPath("$.Message").value("Bad Request"))
        		.andExpect(jsonPath("$.Errors").value("Min price must not be greater than Max price"));
    }
    
    @Test
    @DisplayName("GET /api/products/paged - Should return 400 BadRequestException when Invalid Sort Property provided")
    void shouldReturn400ForBadRequestExceptionForInvalidSortProperty() throws Exception {
 
        when(productService.getProductsPaginated(any(Pageable.class)))
        	.thenThrow(new PropertyReferenceException("foo", 
        			TypeInformation.of(Product.class), Collections.emptyList()));
        
        ResultActions perform = mockMvc.perform(get("/api/products/paged")
        		.contentType(MediaType.APPLICATION_JSON)
        		.accept(MediaType.APPLICATION_JSON)
        		.queryParam("sort", "foo"));
        perform.andExpect(status().isBadRequest())
        		.andExpect(jsonPath("$.StatusCode").value(400))
        		.andExpect(jsonPath("$.Message").value("Bad Request"))
        		.andExpect(jsonPath("$.Errors").value("Invalid Sort property: foo"));
    }
    
    @Test
    @DisplayName("GET /api/products/search - Should return 400 BadRequestException when product name not provided in search")
    void shouldReturn400ForBadRequestExceptionForSearchRequest() throws Exception {
    	
        ResultActions perform = mockMvc.perform(get("/api/products/search")
        		.contentType(MediaType.APPLICATION_JSON)
        		.accept(MediaType.APPLICATION_JSON).queryParam("productName", " "));
        perform.andExpect(status().isBadRequest())
        		.andExpect(jsonPath("$.StatusCode").value(400))
        		.andExpect(jsonPath("$.Message").value("Bad Request"))
        		.andExpect(jsonPath("$.Errors").value("Product Name cannot be blank."
        				+ " Please try again by Providing a Product Name."));
    }
    
    
  
}
