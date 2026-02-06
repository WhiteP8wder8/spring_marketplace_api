package com.api.spring_marketplace_api.repository;

import com.api.spring_marketplace_api.model.entity.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {

    @Query("""
    SELECT o FROM Order o
    LEFT JOIN FETCH o.orderItems i
    LEFT JOIN FETCH i.product
    WHERE o.id = :orderId
    """)
    Optional<Order> findByIdWithItems(@Param("orderId") Long orderId);

    List<Order> findAllByOwnerIdOrderByCreatedAtDesc(UUID ownerId);
}
