package com.api.spring_marketplace_api.model.dto;

import com.api.spring_marketplace_api.model.entity.Order;
import com.api.spring_marketplace_api.model.entity.OrderItem;

import java.util.List;

public class OrderMapper {

    private OrderMapper() {}

    public static OrderResponseDto toDto(Order order) {
        List<OrderItemResponseDto> itemDtos = order.getOrderItems().stream()
                .map(OrderMapper::toItemDto)
                .toList();

        return new OrderResponseDto(
                order.getId(),
                order.getStatus(),
                order.getTotalPrice(),
                order.getCreatedAt(),
                itemDtos
        );
    }

    private static OrderItemResponseDto toItemDto(OrderItem orderItem) {
        return new OrderItemResponseDto(
                orderItem.getProduct().getId(),
                orderItem.getProduct().getTitle(),
                orderItem.getQuantity(),
                orderItem.getPricePerUnit()
        );
    }
}
