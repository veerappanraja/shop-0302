package com.ecommerce.orders.controller;

import com.ecommerce.model.Order;
import com.ecommerce.orders.dto.CreateOrderRequest;
import com.ecommerce.orders.dto.OrderResponse;
import com.ecommerce.orders.service.OrderService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/orders")
public class OrderController {

    private final OrderService orderService;

    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    @PostMapping
    public OrderResponse createOrder(@RequestBody CreateOrderRequest request) {
        return orderService.createOrder(request);
    }

    @GetMapping("/{order_id}")
    public OrderResponse getOrder(@PathVariable("order_id") Long orderId) {
        return orderService.getOrder(orderId);
    }

    @GetMapping
    public List<Order> listOrders() {
        return orderService.listOrders();
    }
}
