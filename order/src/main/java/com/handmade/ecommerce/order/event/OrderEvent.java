package com.handmade.ecommerce.order.event;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.time.LocalDateTime;

@Data
@SuperBuilder
@NoArgsConstructor
public abstract class OrderEvent {
    private String eventId;
    private String orderId;
    private String customerId;
    private LocalDateTime timestamp;
    private String orderStatus;
} 