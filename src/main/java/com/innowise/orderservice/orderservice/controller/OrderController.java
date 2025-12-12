package com.innowise.orderservice.orderservice.controller;

import com.innowise.orderservice.orderservice.model.request.OrderRequest;
import com.innowise.orderservice.orderservice.model.response.OrderResponse;
import com.innowise.orderservice.orderservice.service.OrderService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("orders")
@RequiredArgsConstructor
public class OrderController {
    private final OrderService orderService;

    @PostMapping("/create")
    public ResponseEntity<OrderResponse> findAll(@RequestBody @Valid OrderRequest orderRequest) {
        return ResponseEntity.ok(orderService.createOrder(orderRequest));
    }

    @GetMapping("/{id}")
    public ResponseEntity<OrderResponse> findById(@PathVariable Long id) {
        return ResponseEntity.ok(orderService.getOrder(id));
    }

    @GetMapping("/find")
    public ResponseEntity<List<OrderResponse>> findByIds(@RequestParam List<Long> ids) {
        return ResponseEntity.ok(orderService.getOrdersByIds(ids));
    }

    @GetMapping("/findbystatus/{status}")
    public ResponseEntity<List<OrderResponse>> findByStatus(@PathVariable String status) {
        return ResponseEntity.ok(orderService.getAllOrdersByStatus(status));
    }

    @PostMapping("/update/{id}")
    public ResponseEntity<OrderResponse> update(@PathVariable Long id, @RequestBody @Valid OrderRequest orderRequest) {
        return ResponseEntity.ok(orderService.updateOrder(id, orderRequest));
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<OrderResponse> delete(@PathVariable Long id) {
        orderService.deleteOrder(id);
        return ResponseEntity.noContent().build();
    }

}
