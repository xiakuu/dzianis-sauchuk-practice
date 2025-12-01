package com.innowise.orderservice.orderservice.model.request;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class OrderItemRequest {
    @NotNull
    private Long orderId;
    @NotNull
    private Long itemId;
    @Min(0L)
    private int quantity;
}
