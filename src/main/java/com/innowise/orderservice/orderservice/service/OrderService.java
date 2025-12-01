package com.innowise.orderservice.orderservice.service;

import com.innowise.orderservice.orderservice.exceptions.ResourceNotFoundException;
import com.innowise.orderservice.orderservice.model.Orders;
import com.innowise.orderservice.orderservice.model.request.OrderRequest;
import com.innowise.orderservice.orderservice.model.response.OrderResponse;
import com.innowise.orderservice.orderservice.repository.OrderRepository;
import com.innowise.orderservice.orderservice.repository.mapper.OrderMapper;
import jakarta.transaction.Transactional;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class OrderService {
    private final OrderRepository orderRepository;
    private final OrderMapper orderMapper;
    private final UserService userService;

    public OrderResponse createOrder(OrderRequest orderRequest) {
        Orders orders = orderMapper.toOrders(orderRequest);
        orderRepository.save(orders);
        return orderMapper.toOrderResponse(orders, userService.getUser(orderRequest.getUserId()));
    }

    @Transactional
    public OrderResponse getOrder(Long id) {
        Orders order = orderRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Order by id " + id + " not found"));
        return orderMapper.toOrderResponse(order, userService.getUser(order.getUserId()));
    }

    @Transactional
    public List<OrderResponse> getOrdersByIds(List<Long> ids) {

            if(orderRepository.findAllById(ids).size() != ids.size()) {
                throw new ResourceNotFoundException("Orders by ids " + ids + " not found");
            }
            return orderRepository.findAllById(ids).stream()
                    .map(order -> orderMapper.toOrderResponse(order, userService.getUser(order.getUserId())))
                    .collect(Collectors.toList());
        }

        @Transactional
    public List<OrderResponse> getAllOrdersByStatus(String status) {
        if(orderRepository.findOrdersByStatus(status).isEmpty()) {
            throw new ResourceNotFoundException("Orders by status " + status + " not found");
        }
             return orderRepository.findOrdersByStatus(status).stream()
                    .map(order -> orderMapper.toOrderResponse(order, userService.getUser(order.getUserId())))
                    .collect(Collectors.toList());
    }

    @Transactional
    public OrderResponse updateOrder(Long id, OrderRequest orderRequest) {
        Orders exOrder = orderRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Order by id " + id + " not found"));
        Orders order = orderMapper.toOrders(orderRequest);
        order.setCreationDate(exOrder.getCreationDate());
        order.setId(id);
        orderRepository.save(order);
        return orderMapper.toOrderResponse(order, userService.getUser(order.getUserId()));
    }

    @Transactional
    public void deleteOrder(Long id) {
        if (!orderRepository.existsById(id)) {
            throw new ResourceNotFoundException("Order by id " + id + " not found");
        }
        orderRepository.deleteById(id);
    }
}
