package com.api.spring_marketplace_api.service;

import com.api.spring_marketplace_api.model.dto.ProductMapper;
import com.api.spring_marketplace_api.model.dto.ProductResponseDto;
import com.api.spring_marketplace_api.model.entity.Product;
import com.api.spring_marketplace_api.repository.ProductsRepository;
import com.api.spring_marketplace_api.util.AdminUtil;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ProductService {

    private final ProductsRepository productsRepository;

    public Page<ProductResponseDto> getAllProducts(
            int page,
            int size,
            String sortBy,
            String direction
    ) {
        size = Math.min(size, 50);

        Sort.Direction dir;
        try {
            dir = Sort.Direction.valueOf(direction.toUpperCase());
        } catch (Exception e) {
            dir = Sort.Direction.DESC;
        }

        Pageable pageable = PageRequest.of(page, size, Sort.by(dir, sortBy));

        return productsRepository.findAll(pageable).map(ProductMapper::toDto);
    }

    public ProductResponseDto getProduct(long productId) {
        Product product = productsRepository.findById(productId).orElseThrow(
                () -> new EntityNotFoundException("Product with id " + productId + " not found")
        );

        return ProductMapper.toDto(product);
    }

    @PreAuthorize("hasAnyRole('SELLER', 'ADMIN')")
    @Transactional
    public ProductResponseDto createProduct(String title,
                                 String description,
                                 Integer quantity,
                                 BigDecimal price,
                                 UUID ownerId
    ) {
        Product newProduct = new Product();
        newProduct.setTitle(title);
        newProduct.setDescription(description);
        newProduct.setQuantity(quantity);
        newProduct.setPrice(price);
        newProduct.setOwnerId(ownerId);

        productsRepository.save(newProduct);

        return ProductMapper.toDto(newProduct);
    }

    @PreAuthorize("hasAnyRole('SELLER', 'ADMIN')")
    @Transactional
    public ProductResponseDto patchProduct(
            long productId,
            String title,
            String description,
            Integer quantity,
            BigDecimal price,
            UUID ownerId
    ) {
        Product product = productsRepository.findById(productId).orElseThrow(() ->
                new EntityNotFoundException("Product with id " + productId + " not found"));

        if (!ownerId.equals(product.getOwnerId()) && !AdminUtil.isAdmin()) {
            throw new AccessDeniedException("You can't patch this product. You are not a owner");
        }

        if (title != null) {
            product.setTitle(title);
        }
        if (description != null) {
            product.setDescription(description);
        }
        if (quantity != null) {
            product.setQuantity(quantity);
        }
        if (price != null) {
            product.setPrice(price);
        }

        return ProductMapper.toDto(product);
    }

    @PreAuthorize("hasAnyRole('SELLER', 'ADMIN')")
    @Transactional
    public void deleteOwnProduct(long productId, UUID userId) {
        Product product = productsRepository.findById(productId)
                .orElseThrow(() ->
                        new EntityNotFoundException("Product with id " + productId + " not found"));

        if (!userId.equals(product.getOwnerId()) && !AdminUtil.isAdmin()) {
            throw new AccessDeniedException("You can't delete this product. You are not a owner");
        }

        productsRepository.delete(product);
    }
}
