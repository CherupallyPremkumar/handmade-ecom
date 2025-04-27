package com.handmade.ecommerce.shipping.event;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.time.LocalDateTime;

@Data
@SuperBuilder
@NoArgsConstructor
public abstract class ShipmentEvent {
    private String eventId;
    private String shipmentId;
    private String orderId;
    private LocalDateTime timestamp;
    private String carrier;
    private String trackingNumber;
} 