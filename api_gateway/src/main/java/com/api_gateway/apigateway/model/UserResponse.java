package com.api_gateway.apigateway.model;

import java.time.LocalDate;

public record UserResponse(Long id, String name, String surname, LocalDate birth_date, String email) {
}
