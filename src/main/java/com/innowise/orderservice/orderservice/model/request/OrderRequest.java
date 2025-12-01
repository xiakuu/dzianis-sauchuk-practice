package com.innowise.orderservice.orderservice.model.request;

import com.innowise.orderservice.orderservice.model.OrderItems;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.util.List;

@Data
public class OrderRequest {
    @NotNull
    private Long userId;
    private String status;
    private List<OrderItems> orderItems;
}
