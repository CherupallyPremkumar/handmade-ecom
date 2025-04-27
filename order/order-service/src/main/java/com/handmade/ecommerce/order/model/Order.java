package com.handmade.ecommerce.order.model;

import org.chenile.base.model.BaseEntity;
import org.chenile.base.model.Entity;
import lombok.Data;
import lombok.EqualsAndHashCode;
import java.time.LocalDateTime;
import java.util.List;

@Data
@EqualsAndHashCode(callSuper = true)
@Entity
public class Order extends BaseEntity {
    private String userId;
    private String cartId;
    private List<OrderItem> items;
    private double totalAmount;
    private String status; // PENDING, CONFIRMED, SHIPPED, DELIVERED, CANCELLED
    private String shippingAddress;
    private String billingAddress;
    private String paymentMethod;
    private String paymentStatus; // PENDING, PAID, FAILED
    private LocalDateTime orderDate;
    private LocalDateTime deliveryDate;
    private String trackingNumber;
    private String notes;

    @Override
    public void prePersist() {
        super.prePersist();
        orderDate = LocalDateTime.now();
    }
} 