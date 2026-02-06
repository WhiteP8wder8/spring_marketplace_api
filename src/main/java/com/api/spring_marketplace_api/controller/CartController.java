package com.api.spring_marketplace_api.controller;

import com.api.spring_marketplace_api.model.dto.AddToCartDto;
import com.api.spring_marketplace_api.model.dto.CartDto;
import com.api.spring_marketplace_api.service.CartService;
import com.api.spring_marketplace_api.util.JwtUtil;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/cart")
@RequiredArgsConstructor
public class CartController {

    private final CartService cartService;

    @GetMapping
    public ResponseEntity<CartDto> getCart(
            @AuthenticationPrincipal Jwt jwt
    ) {
        UUID userId = JwtUtil.getUserId(jwt);
        return ResponseEntity.ok(cartService.getUserCart(userId));
    }

    @PostMapping("/items")
    public ResponseEntity<CartDto> addToCart(
            @AuthenticationPrincipal Jwt jwt,
            @RequestBody @Valid AddToCartDto addToCartDto
            ) {
        UUID userId = JwtUtil.getUserId(jwt);

        CartDto updatedCart = cartService.addToCart(
                userId,
                addToCartDto.productId(),
                addToCartDto.quantity()
        );

        return ResponseEntity.ok(updatedCart);
    }

    @DeleteMapping("/items/{cartItemId}")
    public ResponseEntity<Void> deleteItem(
            @AuthenticationPrincipal Jwt jwt,
            @PathVariable Long cartItemId
    ) {
        UUID userId = JwtUtil.getUserId(jwt);
        cartService.deleteCartItem(userId, cartItemId);
        return ResponseEntity.noContent().build();
    }
}
