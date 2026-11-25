package com.example.springBoot.jpa.h2.config;

import java.io.IOException;
import java.time.LocalDateTime;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;

import com.example.springBoot.jpa.h2.exceptions.ErrorResponse;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import tools.jackson.databind.json.JsonMapper;
import tools.jackson.dataformat.xml.XmlMapper;

@Component
public class RestAccessDeniedHandler implements AccessDeniedHandler {

	private final JsonMapper jsonMapper;
	private final XmlMapper xmlMapper;

	public RestAccessDeniedHandler(JsonMapper jsonMapper, XmlMapper xmlMapper) {
		this.jsonMapper = jsonMapper;
		this.xmlMapper = xmlMapper;
	}

	@Override
	public void handle(HttpServletRequest request, HttpServletResponse response,
			AccessDeniedException accessDeniedException) throws IOException, ServletException {

		response.setStatus(HttpServletResponse.SC_FORBIDDEN);
		String accept = request.getHeader("Accept");

		ErrorResponse<String> errorResponse = new ErrorResponse<>(
				HttpStatus.FORBIDDEN.value(),
				"Access denied",
				LocalDateTime.now());

		if (accept != null && accept.contains("application/xml")) {
			response.setContentType(MediaType.APPLICATION_XML_VALUE);
			xmlMapper.writeValue(response.getOutputStream(), errorResponse);
		} else {
			response.setContentType(MediaType.APPLICATION_JSON_VALUE);
			jsonMapper.writeValue(response.getOutputStream(), errorResponse);
		}
	}
}
