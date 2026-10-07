package com.example.springBoot.jpa.h2.config;

import org.springframework.boot.autoconfigure.jackson.Jackson2ObjectMapperBuilderCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.MapperFeature;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.SerializationFeature;

@Configuration
public class JacksonConfig {

	@Bean
	public Jackson2ObjectMapperBuilderCustomizer caseInsensitiveCustomizer() {
		return builder -> {

			// 1. Case insensitivity for incoming requests (Accept case-insensitive keys
			// ("name", "Name", "NAME"))
			builder.featuresToEnable(MapperFeature.ACCEPT_CASE_INSENSITIVE_PROPERTIES);

			// 2. Fail or ignore unknown properties (Prevent API failure when unknown JSON
			// fields are passed)
//            builder.featuresToDisable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);

			// 3. Pretty print JSON responses in development
//            builder.featuresToEnable(SerializationFeature.INDENT_OUTPUT);

			// 4. Custom date-time format globally
//            builder.simpleDateFormat("yyyy-MM-dd HH:mm:ss");

			// Format dates as ISO strings instead of numeric timestamps
//            builder.featuresToDisable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
			
			// Output PascalCase/UpperCamelCase ("StatusCode", "Price", "Name")
			builder.propertyNamingStrategy(PropertyNamingStrategies.UPPER_CAMEL_CASE);
		};
	}

}
/** Deep Dive: How Jackson2ObjectMapperBuilderCustomizer Works in Spring Boot
1. The Core Problem It Solves
Spring Boot uses Jackson’s ObjectMapper under the hood to serialize Java objects to JSON/XML and deserialize incoming request bodies into Java classes.

Out of the box, Spring Boot applies extensive default configurations:

Registers the JavaTimeModule for java.time.LocalDateTime and LocalDate

Configures parameter-name discovery for constructor bindings and Java Records

Sets up standard HTTP message converters for Spring MVC

The Traditional Anti-Pattern:
Previously, if a developer wanted to enable a single feature (such as case-insensitive property matching), they often 
defined their own ObjectMapper bean:

Java
// AVOID: Overrides and breaks Spring Boot defaults
@Bean
public ObjectMapper objectMapper() {
    ObjectMapper mapper = new ObjectMapper();
    mapper.enable(MapperFeature.ACCEPT_CASE_INSENSITIVE_PROPERTIES);
    return mapper; // Default date serializers, Record support, etc., are lost!
}
Defining a custom ObjectMapper directly replaces Spring Boot’s auto-configured bean entirely, which drops all out-of-the-box modules unless re-configured by hand.

2. The Solution: Jackson2ObjectMapperBuilderCustomizer
Jackson2ObjectMapperBuilderCustomizer is a callback interface provided by Spring Boot. It allows modifying 
the builder configuration additively before the final ObjectMapper is built, preserving all Spring Boot defaults.

Interface Definition:
Java
@FunctionalInterface
public interface Jackson2ObjectMapperBuilderCustomizer {
    void customize(Jackson2ObjectMapperBuilder jacksonObjectMapperBuilder);
}

3. Startup Lifecycle & Execution Flow
Plaintext
Application Starts
       │
       ▼
1. Spring Boot creates Jackson2ObjectMapperBuilder (preloaded with defaults)
       │
       ▼
2. Spring scans the ApplicationContext for all beans implementing 
   Jackson2ObjectMapperBuilderCustomizer
       │
       ▼
3. Executes customizer.customize(builder) for each customizer bean found
       │
       ▼
4. Calls builder.build() to generate the final ObjectMapper instance
       │
       ▼
ObjectMapper registered in Spring Context with defaults + your custom rules intact */
