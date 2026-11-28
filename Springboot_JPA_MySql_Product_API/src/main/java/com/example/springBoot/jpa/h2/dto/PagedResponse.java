package com.example.springBoot.jpa.h2.dto;

import java.util.List;

import org.springframework.data.domain.Page;

import com.fasterxml.jackson.annotation.JsonProperty;

public record PagedResponse<T>(
		
		@JsonProperty("Content") List<T> content,
		@JsonProperty("Page") PageInfo page) {
	
	public static<T> PagedResponse<T> from(Page<T> source) {
		return new PagedResponse<>(source.getContent(), 
				new PageInfo(source.getSize(), source.getNumber(),
						source.getTotalElements(), source.getTotalPages()));
	}

}
