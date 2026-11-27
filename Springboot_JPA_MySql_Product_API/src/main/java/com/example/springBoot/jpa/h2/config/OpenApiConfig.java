package com.example.springBoot.jpa.h2.config;

import org.springframework.context.annotation.Configuration;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.security.SecurityScheme;

@Configuration
@SecurityScheme(name = "basicAuth", type = SecuritySchemeType.HTTP,scheme = "basic")
@OpenAPIDefinition(security = @SecurityRequirement(name = "basicAuth"), 
info = @Info(title="Product API", version = "1.2.3"))
public class OpenApiConfig {

}

/**
 * @SecurityScheme(name = "basicAuth", type = SecuritySchemeType.HTTP, scheme =
 *                      "basic"). This is the documented way to get the
 *                      Authorize button for HTTP Basic with springdoc.
 * To make Swagger actually send the credentials with the calls, the operations 
 * must declare that they require this scheme. The simplest is one global
 * requirement: @OpenAPIDefinition(security = @SecurityRequirement(name = "basicAuth"))
 * on the same class.
 * 
 * name = "basicAuth" is just an identifier. The scheme and the requirement are linked by 
 * this name, so it must be spelled identically in both places.
 * type = HTTP with scheme = "basic" tells Swagger UI: "ask for a username and password and
 * send them in an Authorization: Basic ... header".
 */
