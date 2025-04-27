package com.handmade.ecommerce.order.api.service;

import com.handmade.ecommerce.order.api.dto.OrderDTO;
import java.util.List;

public interface OrderApiService {
    OrderDTO createOrder(String userId, String cartId);
    OrderDTO getOrderById(String id);
    List<OrderDTO> getOrdersByUserId(String userId);
    List<OrderDTO> getAllOrders();
    OrderDTO updateOrderStatus(String orderId, String status);
    OrderDTO updatePaymentStatus(String orderId, String paymentStatus);
    OrderDTO updateTrackingInfo(String orderId, String trackingNumber);
    OrderDTO cancelOrder(String orderId);
    void deleteOrder(String orderId);
    double calculateOrderTotal(String orderId);
} 