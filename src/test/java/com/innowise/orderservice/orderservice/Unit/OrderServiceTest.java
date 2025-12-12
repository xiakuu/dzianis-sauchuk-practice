package com.innowise.orderservice.orderservice.Unit;

import com.innowise.orderservice.orderservice.exceptions.ResourceNotFoundException;
import com.innowise.orderservice.orderservice.model.Orders;
import com.innowise.orderservice.orderservice.model.request.OrderRequest;
import com.innowise.orderservice.orderservice.model.response.OrderResponse;
import com.innowise.orderservice.orderservice.model.response.UserResponse;
import com.innowise.orderservice.orderservice.repository.OrderRepository;
import com.innowise.orderservice.orderservice.repository.mapper.OrderMapper;
import com.innowise.orderservice.orderservice.service.OrderService;
import com.innowise.orderservice.orderservice.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
public class OrderServiceTest {
    @Mock
    private OrderMapper orderMapper;
    @Mock
    private OrderRepository orderRepository;
    @Mock
    private UserService userService;

    @InjectMocks
    private OrderService orderService;

    private Orders order;
    private OrderResponse orderResponse;
    private OrderRequest orderRequest;
    private Long orderId;
    private Long userId;
    private UserResponse userResponse;


    @BeforeEach
    void setUp() {
        orderId = 10L;
        userId = 20L;
        order = new Orders();
        order.setId(orderId);
        order.setStatus("new");
        order.setUserId(userId);
        order.setCreationDate(LocalDateTime.now());

        orderRequest = new OrderRequest();
        orderRequest.setStatus(order.getStatus());
        orderRequest.setUserId(order.getUserId());

        orderResponse = new OrderResponse();
        orderResponse.setCreationDate(order.getCreationDate());
        orderResponse.setStatus(order.getStatus());
        orderResponse.setUserId(order.getUserId());
        orderResponse.setId(order.getId());
        orderResponse.setUser(userResponse);

        userResponse = new UserResponse();
        userResponse.setBirth_date(LocalDate.now().minusYears(1));
        userResponse.setName("John");
        userResponse.setEmail("test@email");
        userResponse.setSurname("Smith");
    }

    @Test
    void createOrder() {
        when(orderRepository.save(order)).thenReturn(order);
        when(orderMapper.toOrderResponse(order, userResponse)).thenReturn(orderResponse);
        when(userService.getUser(order.getUserId())).thenReturn(userResponse);
        when(orderMapper.toOrders(orderRequest)).thenReturn(order);

        orderResponse = orderService.createOrder(orderRequest);
        assertEquals(orderResponse.getUserId(), orderRequest.getUserId());
    }

    @Test
    void getOrder() {
        when(orderRepository.findById(orderId)).thenReturn(Optional.of(order));
        when(orderMapper.toOrderResponse(order, userResponse)).thenReturn(orderResponse);
        when(userService.getUser(order.getUserId())).thenReturn(userResponse);

        OrderResponse orderResponse = orderService.getOrder(orderId);
        assertEquals(orderResponse.getUserId(), orderRequest.getUserId());
    }

    @Test
    void getOrder_NotFoundException(){
        when(orderRepository.findById(orderId)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> orderService.getOrder(orderId));
    }

    @Test
    void getOrdersByIds() {
        Long orderId2 = 10L;
        Orders order2 = new Orders();
        order2.setId(orderId2);
        order2.setUserId(30L);
        UserResponse userResponse2 = new UserResponse();
        OrderResponse orderResponse2 = new OrderResponse();
        when(orderRepository.findAllById(Arrays.asList(orderId, orderId2))).thenReturn(Arrays.asList(order, order2));
        when(orderMapper.toOrderResponse(order, userResponse)).thenReturn(orderResponse);
        when(orderMapper.toOrderResponse(order2, userResponse2)).thenReturn(orderResponse2);
        when(userService.getUser(order.getUserId())).thenReturn(userResponse);
        when(userService.getUser(order2.getUserId())).thenReturn(userResponse2);

        List<OrderResponse> result = orderService.getOrdersByIds(Arrays.asList(orderId, orderId2));

        assertNotNull(result);
        assertEquals(orderResponse, result.get(0));
        assertEquals(orderResponse2, result.get(1));
    }

    @Test
    void getOrdersByIds_NotFoundException(){
        when(orderRepository.findAllById(Arrays.asList(orderId))).thenReturn(Collections.emptyList());
        assertThrows(ResourceNotFoundException.class, () -> orderService.getOrdersByIds(Arrays.asList(orderId)));
    }

    @Test
    void getOrdersByStatus(){
        when(orderRepository.findOrdersByStatus(order.getStatus())).thenReturn(Arrays.asList(order));
        when(orderMapper.toOrderResponse(order, userResponse)).thenReturn(orderResponse);
        when(userService.getUser(order.getUserId())).thenReturn(userResponse);
        List<OrderResponse> result = orderService.getAllOrdersByStatus(order.getStatus());
        assertEquals(result.get(0).getUserId(), orderRequest.getUserId());
    }

    @Test
    void getOrdersByStatus_NotFoundException(){
        when(orderRepository.findOrdersByStatus(order.getStatus())).thenReturn(Collections.emptyList());
        assertThrows(ResourceNotFoundException.class, () -> orderService.getAllOrdersByStatus(order.getStatus()));
    }

    @Test
    void updateOrder() {
        when(orderRepository.save(order)).thenReturn(order);
        when(orderRepository.findById(orderId)).thenReturn(Optional.of(order));
        when(orderMapper.toOrderResponse(order, userResponse)).thenReturn(orderResponse);
        when(orderMapper.toOrders(orderRequest)).thenReturn(order);
        when(userService.getUser(order.getUserId())).thenReturn(userResponse);
        OrderResponse orderResponse = orderService.updateOrder(orderId, orderRequest);
        assertEquals(orderResponse.getUserId(), orderRequest.getUserId());
    }

    @Test
    void updateOrder_NotFoundException(){
        when(orderRepository.findById(orderId)).thenReturn(Optional.empty());
        assertThrows(ResourceNotFoundException.class, () -> orderService.updateOrder(orderId, orderRequest));
    }

    @Test
    void deleteOrder() {
        when(orderRepository.existsById(orderId)).thenReturn(true);
        assertDoesNotThrow(() -> orderService.deleteOrder(orderId));
    }

    @Test
    void deleteOrder_NotFoundException(){
        when(orderRepository.existsById(orderId)).thenReturn(false);
        assertThrows(ResourceNotFoundException.class, () -> orderService.deleteOrder(orderId));
    }

}
