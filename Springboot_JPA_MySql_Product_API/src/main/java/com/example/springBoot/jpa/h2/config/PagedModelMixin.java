package com.example.springBoot.jpa.h2.config;

import org.springframework.data.web.PagedModel.PageMetadata;

import com.fasterxml.jackson.annotation.JsonProperty;

// Mix-in to rename the default "page" property to "Page"
public abstract class PagedModelMixin {
	@JsonProperty("Page")
	abstract PageMetadata getMetadata();
}