package com.example.springBoot.jpa.h2.controller;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.math.BigDecimal;

import com.example.springBoot.jpa.h2.dto.ProductRequest;
import com.fasterxml.jackson.databind.ObjectMapper;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional // Automatically rolls back database writes after each test
@ActiveProfiles("test")
public class ProductControllerIntegrationTest {
	
	@Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    @DisplayName("End-to-End: Create product and retrieve via GET /api/products/search")
    void shouldPersistAndRetrieveProduct() throws Exception {
        ProductRequest request = new ProductRequest("Wireless Presenter", new BigDecimal(45.50));

        // 1. Post to create the product
        mockMvc.perform(post("/api/products")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.Data.Name").value("Wireless Presenter"));

        // 2. Query the search endpoint to confirm persistence
        mockMvc.perform(get("/api/products/search")
                .param("productName", "Wireless Presenter")
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.Data[0].Name").value("Wireless Presenter"))
                .andExpect(jsonPath("$.Data[0].Price").value(45.50));
    }

}
