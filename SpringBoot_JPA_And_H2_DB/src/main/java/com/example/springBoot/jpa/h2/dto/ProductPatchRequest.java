package com.example.springBoot.jpa.h2.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;

import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;

public record ProductPatchRequest(
		@Size(min = 2, max = 100, message = "Product name must be between 2 and 100 characters") 
//		@JacksonXmlProperty(localName = "name")
		@JsonProperty("name")
		String productName,

		@Positive(message = "Price must be greater than zero") 
		Double price) {

}

/** @JacksonXmlProperty(localName = "name") 
 * if we are passing name tag in request then we have to provide localName = name(request tag parameter-name), * Otherwise provide exact productName tag and its value in the request.
 * @JacksonXmlProperty is only honored by the XML mapper.
 * SO USE @JsonProperty("name") as It works for both JSON and XML.*/


/** Why no @NotBlank or @NotNull here?

If the client sends only {"Price": 59.99}, name will de-serialize as null. 
A @NotNull annotation would fail the request, which contradicts the concept of a partial update.

*/
