package com.java.novice.OrderService.repository;

import com.java.novice.OrderService.entity.Order;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface OrderRepository extends JpaRepository<Order,Long> {
    Optional<Order> findById(long id);
    List<Order> findAll();
    Order save(Order order);
}
