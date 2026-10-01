package com.example.springBoot.jpa.h2.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.example.springBoot.jpa.h2.model.Product;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {
	// This Class with extends JpaRepository<Product, Long> is required for
	// h2db/hibernate AND MySQL(real databases).
	// No code needed! JpaRepository provides:
	// save(), findById(), findAll(), deleteById(), count(), etc.

	/**
	 * JpaRepository<Product, Long> takes the entity type (Product) and the primary
	 * key type (Long). Spring automatically creates the runtime implementation
	 * behind the scenes.
	 */

//	BUT->

	/**
	 * In a real app, users don't just fetch by ID or retrieve every single record.
	 * They want to: 
 		Search products by name (e.g., “Find all items containing ‘Mouse’”),
 		 Filter products cheaper than a certain budget, 
 		 Sort results or find an exact match.
 	 * Spring Data JPA lets you write these queries without
	 * writing a single line of implementation code — you just declare method
	 * signatures in your repository interface. 
	 * Spring Data parses the method name and automatically translates it into the underlying SQL query.
	 * 
	 * USE # in a comment block to see all the inbuilt query methods available.
	 * (just write # on a new line of comment block to see all methods available  )
	 * 
	 * Notice you write no SQL or Java implementation code for the first three—Spring inspects 
	 * the keywords (find...By, Containing, IgnoreCase, LessThanEqual) and builds the SQL dynamically.
	 */
	
	// Below are the user defined queries ->
	
	// 1. Exact match: SELECT * FROM products WHERE name = ?
	// List<Product> findByName(String name);
	// We usually don't want to use 1 because user might type in lower or upper case. so better to use 2.
	
    // 2. Case-insensitive search: SELECT * FROM products WHERE LOWER(name) LIKE '%...%'
    List<Product> findByNameContainingIgnoreCase(String keyword);

    // 3. Comparison filter: SELECT * FROM products WHERE price <= ?
    List<Product> findByPriceLessThanEqual(double maxPrice);

    // 4. Custom JPQL Query (for complex logic or joins):
    @Query("SELECT p FROM Product p WHERE p.price BETWEEN :min AND :max")
    List<Product> findProductsInPriceRange(@Param("min") double min, @Param("max") double max);
	
	/** Using Pagination - // Paginated custom search*/
//	Page<Product> findAll(String keyword, Pageable pageable); 
//    JpaRepository already implements PagingAndSortingRepository so no need to explicitly specify for JpaRepository
    // provided methods. HOWEVER User Defined methods needs to specify. Example - .
    // Paginated custom search - 
    Page<Product> findByNameContainingIgnoreCase(String keyword, Pageable pageable); // Not implemented
}
