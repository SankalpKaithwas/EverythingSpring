package com.example.springBoot.jpa.h2.exceptions;

import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonInclude(JsonInclude.Include.NON_NULL)
public record ErrorResponse<T>(
	@JsonProperty("StatusCode")
    int statusCode,
    
    @JsonProperty("Message")
    String message,
    
    @JsonProperty("Errors")
    T errors,
    
    @JsonProperty("Timestamp")
    LocalDateTime timestamp
) {
    // Convenient overloaded constructor for 404/500 (where there is no error map)
    public ErrorResponse(int statusCode, String message, LocalDateTime timestamp) {
        this(statusCode, message, null, timestamp);
    }
}
