package com.api.spring_marketplace_api.service;

import com.api.spring_marketplace_api.model.dto.CartDto;
import com.api.spring_marketplace_api.model.dto.CartItemDto;
import com.api.spring_marketplace_api.model.dto.ProductMapper;
import com.api.spring_marketplace_api.model.entity.Cart;
import com.api.spring_marketplace_api.model.entity.CartItem;
import com.api.spring_marketplace_api.model.entity.Product;
import com.api.spring_marketplace_api.repository.CartRepository;
import com.api.spring_marketplace_api.repository.ProductsRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class CartService {

    private final CartRepository cartRepository;
    private final ProductsRepository productsRepository;

    public CartDto getUserCart(UUID userId) {
        Cart cart = cartRepository.findByOwnerIdWithItems(userId)
                .orElseGet(() -> {
                    Cart newCart = new Cart();
                    newCart.setOwnerId(userId);
                    return cartRepository.save(newCart);
                });

        return mapToDto(cart);
    }

    @Transactional
    public CartDto addToCart(UUID userId, Long productId, int quantity) {
        Cart cart = cartRepository.findByOwnerIdWithItems(userId)
                .orElseGet(() -> {
                    Cart newCart = new Cart();
                    newCart.setOwnerId(userId);
                    return cartRepository.save(newCart);
                });

        Product product = productsRepository.findById(productId)
                .orElseThrow(() -> new EntityNotFoundException("Product with id " + productId + " not found"));

        Optional<CartItem> existingItem = cart.getItems().stream()
                .filter(item -> item.getProduct().getId().equals(productId))
                .findFirst();

        if (existingItem.isPresent()) {
            CartItem item = existingItem.get();
            int newQuantity = item.getQuantity() + quantity;

            checkStock(product, newQuantity);
            item.setQuantity(newQuantity);
        } else {
            checkStock(product, quantity);

            CartItem cartItem = new CartItem();
            cartItem.setCart(cart);
            cartItem.setProduct(product);
            cartItem.setQuantity(quantity);

            cart.getItems().add(cartItem);
        }

        cartRepository.save(cart);
        return mapToDto(cart);
    }

    @Transactional
    public void deleteCartItem(UUID userId, Long cartItemId) {
        Cart cart = cartRepository.findByOwnerIdWithItems(userId)
                .orElseThrow(() -> new EntityNotFoundException("Cart not found"));

        boolean removed = cart.getItems().removeIf(item -> item.getId().equals(cartItemId));

        if (!removed) {
            throw new EntityNotFoundException("CartItem not found in user's cart");
        }

        cartRepository.save(cart);
    }

    private void checkStock(Product product, int requestedQuantity) {
        if (product.getQuantity() < requestedQuantity) {
           throw new IllegalArgumentException("Not enough items in stock. Available " + product.getQuantity());
        }
    }

    private CartDto mapToDto(Cart cart) {
        List<CartItemDto> items = cart.getItems().stream()
                .map(item -> new CartItemDto(
                        item.getId(),
                        item.getQuantity(),
                        item.getProduct().getPrice(),
                        ProductMapper.toDto(item.getProduct())
                ))
                .toList();

        BigDecimal totalPrice = items.stream()
                .map(item -> item.price().multiply(BigDecimal.valueOf(item.quantity())))
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        return new CartDto(items, totalPrice);
    }
}
