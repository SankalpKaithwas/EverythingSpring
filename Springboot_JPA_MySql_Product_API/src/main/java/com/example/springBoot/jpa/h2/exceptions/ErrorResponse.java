package com.example.springBoot.jpa.h2.exceptions;

import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonInclude;

@JsonInclude(JsonInclude.Include.NON_NULL)
public record ErrorResponse<T>(
    int statusCode,
    String message,
    T errors,
    LocalDateTime timestamp
) {
    // Convenient overloaded constructor for 404/500 (where there is no error map)
    public ErrorResponse(int statusCode, String message, LocalDateTime timestamp) {
        this(statusCode, message, null, timestamp);
    }
}

//@JsonInclude(JsonInclude.Include.NON_NULL)
//public record ErrorResponse(
//    int statusCode,
//    String message,
//    Map<String, List<String>> errors,
//    LocalDateTime timestamp
//) {
//    // Convenient overloaded constructor for 404/500 (where there is no error map)
//    public ErrorResponse(int statusCode, String message, LocalDateTime timestamp) {
//        this(statusCode, message, null, timestamp);
//    }
//}
