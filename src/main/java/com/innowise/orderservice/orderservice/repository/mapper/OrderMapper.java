package com.innowise.orderservice.orderservice.repository.mapper;

import com.innowise.orderservice.orderservice.model.Orders;
import com.innowise.orderservice.orderservice.model.request.OrderRequest;
import com.innowise.orderservice.orderservice.model.response.OrderResponse;
import com.innowise.orderservice.orderservice.model.response.UserResponse;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface OrderMapper {
    Orders toOrders(OrderRequest orderRequest);
    @Mapping(source = "userResponse", target = "user")
    OrderResponse toOrderResponse(Orders orders, UserResponse userResponse);
}
