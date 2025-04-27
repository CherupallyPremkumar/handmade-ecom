package com.handmade.ecommerce.shipping.event;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.math.BigDecimal;

@Data
@EqualsAndHashCode(callSuper = true)
@SuperBuilder
@NoArgsConstructor
public class ShipmentCreatedEvent extends ShipmentEvent {
    private String customerId;
    private String serviceLevel;
    private BigDecimal shippingCost;
    private String currency;
    private String fromAddress;
    private String toAddress;
} 