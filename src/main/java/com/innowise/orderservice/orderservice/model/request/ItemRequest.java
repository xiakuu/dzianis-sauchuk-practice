package com.innowise.orderservice.orderservice.model.request;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class ItemRequest {
    @NotNull
    private String name;
    @NotNull
    private int price;
}
