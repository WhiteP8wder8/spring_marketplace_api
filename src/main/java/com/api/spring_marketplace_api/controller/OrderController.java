package com.api.spring_marketplace_api.controller;

import com.api.spring_marketplace_api.model.dto.OrderResponseDto;
import com.api.spring_marketplace_api.service.OrderService;
import com.api.spring_marketplace_api.util.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("/orders")
public class OrderController {

    private final OrderService orderService;

    @GetMapping
    public ResponseEntity<List<OrderResponseDto>> getOrderHistory(
            @AuthenticationPrincipal Jwt jwt
    ) {
        UUID userId = JwtUtil.getUserId(jwt);
        return ResponseEntity.ok(orderService.getUserOrderHistory(userId));
    }

    @PostMapping("/checkout")
    public ResponseEntity<OrderResponseDto> checkout(
            @AuthenticationPrincipal Jwt jwt
    ) {
        UUID userId = JwtUtil.getUserId(jwt);
        return ResponseEntity.ok(orderService.checkout(userId));
    }
}
