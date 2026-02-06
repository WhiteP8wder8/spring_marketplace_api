package com.api.spring_marketplace_api.model.dto;

import java.math.BigDecimal;

public record OrderItemResponseDto(
        Long productId,
        String productTitle,
        int quantity,
        BigDecimal priceAtPurchase
) {
}
