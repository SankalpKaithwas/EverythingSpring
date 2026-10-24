package com.example.springBoot.jpa.h2.dto;

import java.math.BigDecimal;

import com.fasterxml.jackson.annotation.JsonProperty;

import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;

public record ProductPatchRequest(
		@Size(min = 2, max = 100, message = "Product name must be between 2 and 100 characters") 
//		@JacksonXmlProperty(localName = "name")
		@JsonProperty("name")
		String productName,

		@Positive(message = "Price must be greater than zero") 
		@Digits(integer = 8, fraction = 2,
				message = "Price must be less than 100000000 and within 2 decimal places") 
		BigDecimal price) {
	
	public ProductPatchRequest {
		if (productName != null) {
			productName = productName.trim();
		}
		if (price != null && price.scale() < 2) {
			price = price.setScale(2);
		}
	}
}

/** @JacksonXmlProperty(localName = "name") 
 * if we are passing name tag in request then we have to provide localName = name(request tag parameter-name), 
 * Otherwise provide exact productName tag and its value in the request.
 * @JacksonXmlProperty is only honored by the XML mapper.
 * SO USE @JsonProperty("name") as It works for both JSON and XML.*/


/** Why no @NotBlank or @NotNull here?

If the client sends only {"Price": 59.99}, name will de-serialize as null. 
A @NotNull annotation would fail the request, which contradicts the concept of a partial update.

setScale() sets the limit of decimal place. So 2 means only 2 decimal places but if only 1 digit is provided
after the decimal, then it will add 0 after the 1st digit. Ex- 89.9 will become 89.90. 

*/
