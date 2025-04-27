package com.handmade.ecommerce.order.api.dto;

import lombok.Data;
import java.util.List;

@Data
public class OrderDTO {
    private String id;
    private String userId;
    private String cartId;
    private List<OrderItemDTO> items;
    private double totalAmount;
    private String status;
    private String shippingAddress;
    private String billingAddress;
    private String paymentMethod;
    private String paymentStatus;
    private String orderDate;
    private String deliveryDate;
    private String trackingNumber;
    private String notes;
} 