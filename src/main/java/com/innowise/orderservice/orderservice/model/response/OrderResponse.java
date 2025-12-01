package com.innowise.orderservice.orderservice.model.response;

import com.innowise.orderservice.orderservice.model.OrderItems;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
public class OrderResponse {
    private Long id;
    private Long userId;
    private String status;
    private LocalDateTime creationDate;
    private List<OrderItems> orderItems;
    private UserResponse user;
}
