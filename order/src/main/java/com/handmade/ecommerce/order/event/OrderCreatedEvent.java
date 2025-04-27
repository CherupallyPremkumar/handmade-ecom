package com.handmade.ecommerce.order.event;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.math.BigDecimal;
import java.util.List;

@Data
@EqualsAndHashCode(callSuper = true)
@SuperBuilder
@NoArgsConstructor
public class OrderCreatedEvent extends OrderEvent {
    private List<OrderItemDto> items;
    private BigDecimal totalAmount;
    private String currency;
    private String shippingAddress;
    private String billingAddress;
    private String paymentMethod;
}

@Data
@NoArgsConstructor
public class OrderItemDto {
    private String productId;
    private Integer quantity;
    private BigDecimal price;
    private String currency;
} 