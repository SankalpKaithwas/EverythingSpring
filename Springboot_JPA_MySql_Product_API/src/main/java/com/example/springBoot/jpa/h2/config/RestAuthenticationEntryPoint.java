package com.example.springBoot.jpa.h2.config;

import java.io.IOException;
import java.time.LocalDateTime;

import org.springframework.http.MediaType;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import com.example.springBoot.jpa.h2.exceptions.ErrorResponse;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import tools.jackson.databind.json.JsonMapper;
import tools.jackson.dataformat.xml.XmlMapper;

@Component
public class RestAuthenticationEntryPoint implements AuthenticationEntryPoint {

	private final JsonMapper jsonMapper;
	private final XmlMapper xmlMapper;
//final plus a constructor means the fields can never stay null.

	public RestAuthenticationEntryPoint(JsonMapper jsonMapper, XmlMapper xmlMapper) {
		this.jsonMapper = jsonMapper;
		this.xmlMapper = xmlMapper;
	}

	@Override
	public void commence(HttpServletRequest request, HttpServletResponse response,
			AuthenticationException authException) throws IOException, ServletException {

		response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
		response.setHeader("WWW-Authenticate", "Basic realm=\"Realm\"");

		ErrorResponse<String> errorResponse = new ErrorResponse<>(
				response.getStatus(), 
				"Authentication required",
				LocalDateTime.now());
		
		String accept = request.getHeader("Accept");
		if (accept != null && (accept.contains("application/xml"))) {
			response.setContentType(MediaType.APPLICATION_XML_VALUE);
			xmlMapper.writeValue(response.getOutputStream(), errorResponse);
		} else {
			response.setContentType(MediaType.APPLICATION_JSON_VALUE);
			jsonMapper.writeValue(response.getOutputStream(), errorResponse);
		}
	}
}
