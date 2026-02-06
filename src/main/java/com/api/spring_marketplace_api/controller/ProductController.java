package com.api.spring_marketplace_api.controller;

import com.api.spring_marketplace_api.model.dto.CreateProductDto;
import com.api.spring_marketplace_api.model.dto.PatchProductDto;
import com.api.spring_marketplace_api.model.dto.ProductResponseDto;
import com.api.spring_marketplace_api.service.ProductService;
import com.api.spring_marketplace_api.util.JwtUtil;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("/products")
public class ProductController {

    private final ProductService productService;

    @GetMapping
    public ResponseEntity<Page<ProductResponseDto>> getAllProducts(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size,
            @RequestParam(defaultValue = "title") String sortBy,
            @RequestParam(defaultValue = "DESC") String direction
    ) {
        return ResponseEntity.ok(productService.getAllProducts(page, size, sortBy, direction));
    }

    @GetMapping("/{productId}")
    public ResponseEntity<ProductResponseDto> getProduct(@PathVariable long productId) {
        return ResponseEntity.ok(productService.getProduct(productId));
    }

    @PostMapping
    public ResponseEntity<ProductResponseDto> createProduct(@AuthenticationPrincipal Jwt jwt,
                                                 @Valid @RequestBody CreateProductDto createProductDto) {
        UUID ownerId = JwtUtil.getUserId(jwt);

        return ResponseEntity.status(HttpStatus.CREATED).body(productService.createProduct(
                createProductDto.title(),
                createProductDto.description(),
                createProductDto.quantity(),
                createProductDto.price(),
                ownerId
        ));
    }

    @PatchMapping("/{productId}")
    public ResponseEntity<ProductResponseDto> patchProduct(
            @PathVariable long productId,
            @AuthenticationPrincipal Jwt jwt,
            @RequestBody @Valid PatchProductDto patchProductDto
    ) {
        UUID ownerId = JwtUtil.getUserId(jwt);

        return ResponseEntity.status(HttpStatus.ACCEPTED).body(productService.patchProduct(
                productId,
                patchProductDto.title(),
                patchProductDto.description(),
                patchProductDto.quantity(),
                patchProductDto.price(),
                ownerId
        ));
    }

    @DeleteMapping("/{productId}")
    public ResponseEntity.HeadersBuilder<?> deleteOwnProduct(@PathVariable long productId, @AuthenticationPrincipal Jwt jwt) {
        UUID userId = JwtUtil.getUserId(jwt);

        productService.deleteOwnProduct(productId, userId);
        return ResponseEntity.noContent();
    }
}
