package com.api.spring_marketplace_api.service;

import com.api.spring_marketplace_api.enums.OrderStatus;
import com.api.spring_marketplace_api.model.dto.OrderMapper;
import com.api.spring_marketplace_api.model.dto.OrderResponseDto;
import com.api.spring_marketplace_api.model.entity.*;
import com.api.spring_marketplace_api.repository.CartRepository;
import com.api.spring_marketplace_api.repository.OrderRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class OrderService {

    private final OrderRepository orderRepository;
    private final CartRepository cartRepository;

    public List<OrderResponseDto> getUserOrderHistory(UUID userId) {
        return orderRepository.findAllByOwnerIdOrderByCreatedAtDesc(userId)
                .stream()
                .map(OrderMapper::toDto)
                .toList();
    }

    @Transactional
    public OrderResponseDto checkout(UUID userId) {
        Cart cart = cartRepository.findByOwnerIdWithItems(userId)
                .orElseThrow(() ->
                    new EntityNotFoundException("Cart not found")
                );

        if (cart.getItems().isEmpty()) {
            throw new IllegalStateException("Cart is empty");
        }

        Order newOrder = new Order();
        newOrder.setOwnerId(userId);
        newOrder.setStatus(OrderStatus.CREATED);

        List<OrderItem> orderItems = new ArrayList<>();
        BigDecimal totalPrice = BigDecimal.ZERO;

        for (CartItem cartItem : cart.getItems()) {
            Product product = cartItem.getProduct();

            if (product.getQuantity() < cartItem.getQuantity()) {
                throw new IllegalArgumentException("Not enough stock for: " + product.getTitle());
            }
            product.setQuantity(product.getQuantity() - cartItem.getQuantity());

            OrderItem orderItem = new OrderItem();
            orderItem.setOrder(newOrder);
            orderItem.setProduct(product);
            orderItem.setQuantity(cartItem.getQuantity());
            orderItem.setPricePerUnit(product.getPrice());

            orderItems.add(orderItem);

            BigDecimal itemTotal = product.getPrice().multiply(BigDecimal.valueOf(cartItem.getQuantity()));
            totalPrice = totalPrice.add(itemTotal);
        }

        newOrder.setOrderItems(orderItems);
        newOrder.setTotalPrice(totalPrice);

        Order savedOrder = orderRepository.save(newOrder);

        cart.getItems().clear();
        cartRepository.save(cart);

        return OrderMapper.toDto(savedOrder);
    }
}
