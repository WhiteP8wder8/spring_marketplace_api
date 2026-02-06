package com.api.spring_marketplace_api.model.dto;

import java.math.BigDecimal;
import java.util.List;

public record CartDto(
        List<CartItemDto> items,
        BigDecimal finalPrice
) {
}
