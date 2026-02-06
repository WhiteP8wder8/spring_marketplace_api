package com.api.spring_marketplace_api.model.dto;

import com.api.spring_marketplace_api.model.entity.Product;

public class ProductMapper {

    private ProductMapper() {}

    public static ProductResponseDto toDto(Product product) {
        return new ProductResponseDto(
                product.getId(),
                product.getTitle(),
                product.getDescription(),
                product.getQuantity(),
                product.getPrice()
        );
    }
}
