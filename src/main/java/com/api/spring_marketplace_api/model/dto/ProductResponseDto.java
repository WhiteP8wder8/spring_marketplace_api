package com.api.spring_marketplace_api.model.dto;

import java.math.BigDecimal;

public record ProductResponseDto(
        Long id,
        String title,
        String description,
        int quantity,
        BigDecimal price
) {
}
