package com.handmade.ecommerce.order.api.service.impl;

import com.handmade.ecommerce.order.api.dto.OrderDTO;
import com.handmade.ecommerce.order.api.dto.OrderItemDTO;
import com.handmade.ecommerce.order.api.service.OrderApiService;
import com.handmade.ecommerce.order.model.Order;
import com.handmade.ecommerce.order.model.OrderItem;
import com.handmade.ecommerce.order.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class OrderApiServiceImpl implements OrderApiService {

    @Autowired
    private OrderService orderService;

    @Override
    public OrderDTO createOrder(String userId, String cartId) {
        Order order = orderService.createOrder(userId, cartId);
        return convertToDTO(order);
    }

    @Override
    public OrderDTO getOrderById(String id) {
        Order order = orderService.getOrderById(id);
        return convertToDTO(order);
    }

    @Override
    public List<OrderDTO> getOrdersByUserId(String userId) {
        return orderService.getOrdersByUserId(userId).stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    @Override
    public List<OrderDTO> getAllOrders() {
        return orderService.getAllOrders().stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    @Override
    public OrderDTO updateOrderStatus(String orderId, String status) {
        Order order = orderService.updateOrderStatus(orderId, status);
        return convertToDTO(order);
    }

    @Override
    public OrderDTO updatePaymentStatus(String orderId, String paymentStatus) {
        Order order = orderService.updatePaymentStatus(orderId, paymentStatus);
        return convertToDTO(order);
    }

    @Override
    public OrderDTO updateTrackingInfo(String orderId, String trackingNumber) {
        Order order = orderService.updateTrackingInfo(orderId, trackingNumber);
        return convertToDTO(order);
    }

    @Override
    public OrderDTO cancelOrder(String orderId) {
        Order order = orderService.cancelOrder(orderId);
        return convertToDTO(order);
    }

    @Override
    public void deleteOrder(String orderId) {
        orderService.deleteOrder(orderId);
    }

    @Override
    public double calculateOrderTotal(String orderId) {
        return orderService.calculateOrderTotal(orderId);
    }

    private OrderDTO convertToDTO(Order order) {
        OrderDTO dto = new OrderDTO();
        dto.setId(order.getId());
        dto.setUserId(order.getUserId());
        dto.setCartId(order.getCartId());
        dto.setItems(order.getItems().stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList()));
        dto.setTotalAmount(order.getTotalAmount());
        dto.setStatus(order.getStatus());
        dto.setShippingAddress(order.getShippingAddress());
        dto.setBillingAddress(order.getBillingAddress());
        dto.setPaymentMethod(order.getPaymentMethod());
        dto.setPaymentStatus(order.getPaymentStatus());
        dto.setOrderDate(order.getOrderDate().toString());
        dto.setDeliveryDate(order.getDeliveryDate() != null ? order.getDeliveryDate().toString() : null);
        dto.setTrackingNumber(order.getTrackingNumber());
        dto.setNotes(order.getNotes());
        return dto;
    }

    private OrderItemDTO convertToDTO(OrderItem item) {
        OrderItemDTO dto = new OrderItemDTO();
        dto.setId(item.getId());
        dto.setProductId(item.getProductId());
        dto.setProductName(item.getProductName());
        dto.setQuantity(item.getQuantity());
        dto.setUnitPrice(item.getUnitPrice());
        dto.setTotalPrice(item.getTotalPrice());
        dto.setOrderId(item.getOrderId());
        dto.setStatus(item.getStatus());
        return dto;
    }
} 