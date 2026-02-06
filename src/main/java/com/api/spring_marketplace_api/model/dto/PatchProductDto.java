package com.api.spring_marketplace_api.model.dto;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Size;

import java.math.BigDecimal;

public record PatchProductDto(

        @Size(max = 50)
        String title,
        @Size(max = 300)
        String description,
        @Min(0)
        Integer quantity,
        @DecimalMin("0.0")
        BigDecimal price
) {
}
