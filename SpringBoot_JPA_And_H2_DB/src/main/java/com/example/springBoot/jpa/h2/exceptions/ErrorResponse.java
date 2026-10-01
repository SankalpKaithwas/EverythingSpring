package com.example.springBoot.jpa.h2.exceptions;

import java.time.LocalDateTime;

public record ErrorResponse(int statusCode, String error, String message, LocalDateTime timestamp) {

}
