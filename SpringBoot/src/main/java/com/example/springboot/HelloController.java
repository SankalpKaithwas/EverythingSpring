package com.example.springboot;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/** This is a Test COntroller. This was created at the beginning. */
@RestController
public class HelloController {

	public record MessageResponse(String status, String message, long timestamp) {
	}

	// Simple GET mapping: http://localhost:8080/greet
	@GetMapping("/greet")
	public String greet() {
		return "Spring Boot application is working!";
	}

	// Dynamic parameter: http://localhost:8080/hello?name=Sankalp
	@GetMapping("/hello")
	public String sayHello(@RequestParam(defaultValue = "World") String name) {
		return String.format("Hello, %s!", name);
	}

	@GetMapping("/api/status")
	public MessageResponse getStatus() {
		return new MessageResponse("SUCCESS", "API is live and healthy", System.currentTimeMillis());
	}
}
