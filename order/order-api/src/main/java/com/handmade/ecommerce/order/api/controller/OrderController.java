package com.handmade.ecommerce.order.api.controller;

import com.handmade.ecommerce.order.api.dto.OrderDTO;
import com.handmade.ecommerce.order.api.service.OrderApiService;
import org.chenile.base.controller.ChenileController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/orders")
@ChenileController
public class OrderController {

    @Autowired
    private OrderApiService orderApiService;

    @PostMapping
    public ResponseEntity<OrderDTO> createOrder(@RequestParam String userId, @RequestParam String cartId) {
        return ResponseEntity.ok(orderApiService.createOrder(userId, cartId));
    }

    @GetMapping("/{id}")
    public ResponseEntity<OrderDTO> getOrder(@PathVariable String id) {
        return ResponseEntity.ok(orderApiService.getOrderById(id));
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<List<OrderDTO>> getOrdersByUserId(@PathVariable String userId) {
        return ResponseEntity.ok(orderApiService.getOrdersByUserId(userId));
    }

    @GetMapping
    public ResponseEntity<List<OrderDTO>> getAllOrders() {
        return ResponseEntity.ok(orderApiService.getAllOrders());
    }

    @PutMapping("/{orderId}/status")
    public ResponseEntity<OrderDTO> updateOrderStatus(@PathVariable String orderId, @RequestParam String status) {
        return ResponseEntity.ok(orderApiService.updateOrderStatus(orderId, status));
    }

    @PutMapping("/{orderId}/payment")
    public ResponseEntity<OrderDTO> updatePaymentStatus(@PathVariable String orderId, @RequestParam String paymentStatus) {
        return ResponseEntity.ok(orderApiService.updatePaymentStatus(orderId, paymentStatus));
    }

    @PutMapping("/{orderId}/tracking")
    public ResponseEntity<OrderDTO> updateTrackingInfo(@PathVariable String orderId, @RequestParam String trackingNumber) {
        return ResponseEntity.ok(orderApiService.updateTrackingInfo(orderId, trackingNumber));
    }

    @PutMapping("/{orderId}/cancel")
    public ResponseEntity<OrderDTO> cancelOrder(@PathVariable String orderId) {
        return ResponseEntity.ok(orderApiService.cancelOrder(orderId));
    }

    @DeleteMapping("/{orderId}")
    public ResponseEntity<Void> deleteOrder(@PathVariable String orderId) {
        orderApiService.deleteOrder(orderId);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/{orderId}/total")
    public ResponseEntity<Double> calculateOrderTotal(@PathVariable String orderId) {
        return ResponseEntity.ok(orderApiService.calculateOrderTotal(orderId));
    }
} 