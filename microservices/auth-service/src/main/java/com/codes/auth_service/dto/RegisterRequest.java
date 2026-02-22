package com.codes.auth_service.dto;

public record RegisterRequest(
        String email,
        String password,
        String name
) {
}
