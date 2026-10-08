package com.example.springBoot.jpa.h2.model;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

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
	 *  GenerationType.IDENTITY tells Hibernate to use MySQL's native AUTO_INCREMENT column mechanism.
	 */

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	private String name;
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
