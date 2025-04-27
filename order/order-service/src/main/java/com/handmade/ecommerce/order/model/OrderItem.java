package com.handmade.ecommerce.order.model;

import org.chenile.base.model.BaseEntity;
import org.chenile.base.model.Entity;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
@Entity
public class OrderItem extends BaseEntity {
    private String productId;
    private String productName;
    private int quantity;
    private double unitPrice;
    private double totalPrice;
    private String orderId;
    private String status; // PENDING, CONFIRMED, SHIPPED, DELIVERED, CANCELLED
} 