package com.innowise.orderservice.orderservice.repository;

import com.innowise.orderservice.orderservice.model.Items;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ItemsRepository extends JpaRepository<Items, Long> {
}
