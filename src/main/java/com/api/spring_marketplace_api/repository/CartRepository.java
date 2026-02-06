package com.api.spring_marketplace_api.repository;

import com.api.spring_marketplace_api.model.entity.Cart;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface CartRepository extends JpaRepository<Cart, Long> {

    @Query("""
    SELECT c FROM Cart c
    LEFT JOIN FETCH c.items i
    LEFT JOIN FETCH i.product p
    WHERE c.ownerId = :ownerId
    """)
    Optional<Cart> findByOwnerIdWithItems(@Param("ownerId")UUID ownerId);
}
