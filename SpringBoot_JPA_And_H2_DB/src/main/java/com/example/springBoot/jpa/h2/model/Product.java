package com.example.springBoot.jpa.h2.model;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;

@Entity
@Table(name = "products")
@JacksonXmlRootElement(localName = "Product")
public class Product {

	/**
	 * @Entity: Instructs JPA/Hibernate to map this class to a database table.
	 * 
	 * @Id & @GeneratedValue: Marks id as the primary key with auto-increment.
	 *     Spring Data JPA mapping an @Entity class to an in-memory H2 database.
	 *  GenerationType.IDENTITY - IDENTITY forces Hibernate to execute the INSERT SQL statement
	 *  immediately on save()/persist() so it can retrieve the generated ID.
	 */

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@NotBlank(message = "Product name is required and cannot be blank")
	@Size(min = 2, max = 100, message = "Product name must be between 2 and 100 characters")
	private String name;

	@NotNull(message = "Price is required")
	@Positive(message = "Price must be greater than zero")
	private Double price;

	// JPA requires a no-argument constructor
	public Product() {
	}

	public Product(String name, Double price) {
		super();
		this.name = name;
		this.price = price;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Double getPrice() {
		return price;
	}

	public void setPrice(Double price) {
		this.price = price;
	}

}
