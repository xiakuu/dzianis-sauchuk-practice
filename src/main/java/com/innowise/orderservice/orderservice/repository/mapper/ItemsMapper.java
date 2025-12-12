package com.innowise.orderservice.orderservice.repository.mapper;

import com.innowise.orderservice.orderservice.model.Items;
import com.innowise.orderservice.orderservice.model.request.ItemRequest;
import com.innowise.orderservice.orderservice.model.response.ItemResponse;
import org.hibernate.cache.spi.support.AbstractReadWriteAccess;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface ItemsMapper {
    ItemResponse toItemResponse(Items items);
    Items toItems(ItemRequest itemRequest);
}
