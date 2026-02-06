package com.api.spring_marketplace_api.model.dto;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

import java.math.BigDecimal;

public record CreateProductDto(
        @NotBlank
        @Size(max = 50)
        String title,
        @Size(max = 300)
        String description,
        @Min(0)
        int quantity,
        @DecimalMin("0.0")
        BigDecimal price
) {
}
