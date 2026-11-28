package com.example.springBoot.jpa.h2.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

public record PageInfo(
		
		@JsonProperty("Size") long size,
		@JsonProperty("Number") long number,
		@JsonProperty("TotalElements") long totalElements,
		@JsonProperty("TotalPages") long totalPages) {

}
