package com.api.spring_marketplace_api.model.dto;

import java.math.BigDecimal;

public record CartItemDto(
        Long id,
        int quantity,
        BigDecimal price,
        ProductResponseDto product
) {
}
