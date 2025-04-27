package com.handmade.ecommerce.order.service;

import org.chenile.base.service.ChenileService;
import com.handmade.ecommerce.order.model.Order;
import java.util.List;

@ChenileService
public interface OrderService {
    Order createOrder(String userId, String cartId);
    Order getOrderById(String id);
    List<Order> getOrdersByUserId(String userId);
    List<Order> getAllOrders();
    Order updateOrderStatus(String orderId, String status);
    Order updatePaymentStatus(String orderId, String paymentStatus);
    Order updateTrackingInfo(String orderId, String trackingNumber);
    Order cancelOrder(String orderId);
    void deleteOrder(String orderId);
    double calculateOrderTotal(String orderId);
} 