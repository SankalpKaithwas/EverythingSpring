package com.example.springBoot.jpa.h2.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.springBoot.jpa.h2.model.Product;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {
	// No code needed! JpaRepository provides:
	// save(), findById(), findAll(), deleteById(), count(), etc.

	/**
	 * JpaRepository<Product, Long> takes the entity type (Product) and the primary
	 * key type (Long). Spring automatically creates the runtime implementation
	 * behind the scenes.
	 */
	
	// This Class with extends JpaRepository<Product, Long> is required for h2db and hibernate
}
