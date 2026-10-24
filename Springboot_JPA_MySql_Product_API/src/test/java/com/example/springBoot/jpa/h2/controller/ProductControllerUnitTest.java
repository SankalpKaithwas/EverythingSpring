package com.example.springBoot.jpa.h2.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.math.BigDecimal;
import java.util.List;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import com.example.springBoot.jpa.h2.config.JacksonConfig;
import com.example.springBoot.jpa.h2.dto.ProductRequest;
import com.example.springBoot.jpa.h2.dto.ProductResponse;
import com.example.springBoot.jpa.h2.exceptions.GlobalExceptionHandler;
import com.example.springBoot.jpa.h2.exceptions.ResourceNotFoundException;
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
            new ProductResponse(1L, "Mechanical Keyboard", new BigDecimal(110.00)),
            new ProductResponse(2L, "Optical Mouse", new BigDecimal(25.00))
        );

        when(productService.getAllProducts()).thenReturn(mockList);

        ResultActions perform = mockMvc.perform(get("/api/products")
                .accept(MediaType.APPLICATION_JSON));
        
        perform.andExpect(status().isOk())
                .andExpect(jsonPath("$.Success").value(true))
                .andExpect(jsonPath("$.Message").value("Products fetched successfully"))
                .andExpect(jsonPath("$.Data[0].Id").value(1))
                .andExpect(jsonPath("$.Data[0].Name").value("Mechanical Keyboard"))
                .andExpect(jsonPath("$.Data[0].Price").value(new BigDecimal(110.00)));
    }
    
    @Test
    @DisplayName("POST /api/products - Should return 201 when payload is valid")
    void shouldCreateProductSuccessfully() throws Exception {
        ProductRequest validRequest = new ProductRequest("Gaming Headset", new BigDecimal("89.99"));
        ProductResponse savedResponse = new ProductResponse(10L, "Gaming Headset", new BigDecimal("89.99"));

        when(productService.addProduct(any(ProductRequest.class))).thenReturn(savedResponse);

        ResultActions result = mockMvc.perform(post("/api/products")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(validRequest)));
        
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
    
}
