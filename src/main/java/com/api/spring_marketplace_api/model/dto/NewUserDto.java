package com.api.spring_marketplace_api.model.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record NewUserDto(
        @Email
        @NotBlank
        String email,
        @NotBlank
        @Size(min = 6)
        String password,
        @NotBlank
        @Size(min = 3)
        String name,
        @NotBlank
        @Size(min = 3)
        String lastName
) {
}
