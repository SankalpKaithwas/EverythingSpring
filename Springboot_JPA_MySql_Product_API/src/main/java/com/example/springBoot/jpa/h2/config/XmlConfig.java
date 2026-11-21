package com.example.springBoot.jpa.h2.config;

import org.springframework.boot.jackson.autoconfigure.XmlMapperBuilderCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.web.PagedModel;

import tools.jackson.databind.MapperFeature;
import tools.jackson.databind.PropertyNamingStrategies;

@Configuration
public class XmlConfig {

	@Bean
	public XmlMapperBuilderCustomizer xmlCaseInsensitiveCustomizer() {
		return builder -> {
			// 1. Case insensitivity for incoming requests (Accept case-insensitive keys
			// ("name", "Name", "NAME"))
			builder.enable(MapperFeature.ACCEPT_CASE_INSENSITIVE_PROPERTIES);

			// 2. Fail or ignore unknown properties (Prevent API failure when unknown JSON
			// fields are passed)
//			            builder.featuresToDisable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);

			// 3. Pretty print JSON responses in development
//			            builder.featuresToEnable(SerializationFeature.INDENT_OUTPUT);

			// 4. Custom date-time format globally
//			            builder.simpleDateFormat("yyyy-MM-dd HH:mm:ss");

			// Format dates as ISO strings instead of numeric timestamps
//			            builder.featuresToDisable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);

			// Output PascalCase/UpperCamelCase ("StatusCode", "Price", "Name")
			builder.propertyNamingStrategy(PropertyNamingStrategies.UPPER_CAMEL_CASE);

			builder.addMixIn(PagedModel.class, PagedModelMixin.class);

		};
	}

}
