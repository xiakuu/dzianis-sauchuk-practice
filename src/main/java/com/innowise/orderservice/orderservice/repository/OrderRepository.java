package com.innowise.orderservice.orderservice.repository;

import com.innowise.orderservice.orderservice.model.Orders;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OrderRepository extends JpaRepository<Orders, Long> {
    List<Orders> findOrdersByStatus(String status);
}
