package com.innowise.orderservice.orderservice.service;

import com.innowise.orderservice.orderservice.repository.ItemsRepository;
import com.innowise.orderservice.orderservice.repository.mapper.ItemsMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ItemsService {
    private final ItemsRepository itemsRepository;
    private final ItemsMapper itemsMapper;

}
