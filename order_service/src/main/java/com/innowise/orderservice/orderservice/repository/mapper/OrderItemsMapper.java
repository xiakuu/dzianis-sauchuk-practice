package com.innowise.orderservice.orderservice.repository.mapper;

import com.innowise.orderservice.orderservice.model.OrderItems;
import com.innowise.orderservice.orderservice.model.request.OrderItemRequest;
import com.innowise.orderservice.orderservice.model.request.OrderRequest;
import com.innowise.orderservice.orderservice.model.response.OrderItemResponse;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface OrderItemsMapper {
    OrderItems toOrderItem(OrderRequest orderRequest);
    OrderItemResponse toOrderItemResponse(OrderItems orderItems);
}
