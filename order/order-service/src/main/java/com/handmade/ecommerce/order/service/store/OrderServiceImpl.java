package com.handmade.ecommerce.order.service.store;

import org.chenile.base.service.ChenileServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.handmade.ecommerce.order.model.Order;
import com.handmade.ecommerce.order.service.OrderService;
import com.handmade.ecommerce.order.configuration.dao.OrderRepository;
import java.util.List;

@Service
public class OrderServiceImpl extends ChenileServiceImpl<Order> implements OrderService {
    
    @Autowired
    private OrderRepository orderRepository;

    @Override
    public Order createOrder(String userId, String cartId) {
        Order order = new Order();
        order.setUserId(userId);
        order.setCartId(cartId);
        order.setStatus("PENDING");
        order.setPaymentStatus("PENDING");
        return store(order);
    }

    @Override
    public Order getOrderById(String id) {
        return retrieve(id);
    }

    @Override
    public List<Order> getOrdersByUserId(String userId) {
        return orderRepository.findByUserId(userId);
    }

    @Override
    public List<Order> getAllOrders() {
        return orderRepository.findAll();
    }

    @Override
    public Order updateOrderStatus(String orderId, String status) {
        Order order = retrieve(orderId);
        order.setStatus(status);
        return store(order);
    }

    @Override
    public Order updatePaymentStatus(String orderId, String paymentStatus) {
        Order order = retrieve(orderId);
        order.setPaymentStatus(paymentStatus);
        return store(order);
    }

    @Override
    public Order updateTrackingInfo(String orderId, String trackingNumber) {
        Order order = retrieve(orderId);
        order.setTrackingNumber(trackingNumber);
        return store(order);
    }

    @Override
    public Order cancelOrder(String orderId) {
        Order order = retrieve(orderId);
        order.setStatus("CANCELLED");
        return store(order);
    }

    @Override
    public void deleteOrder(String orderId) {
        orderRepository.deleteById(orderId);
    }

    @Override
    public double calculateOrderTotal(String orderId) {
        Order order = retrieve(orderId);
        return order.getItems().stream()
                .mapToDouble(item -> item.getTotalPrice())
                .sum();
    }
} 