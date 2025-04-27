package com.handmade.ecommerce.shipping.event;

import com.handmade.ecommerce.shipping.model.ShipmentStatus;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Data
@EqualsAndHashCode(callSuper = true)
@SuperBuilder
@NoArgsConstructor
public class ShipmentStatusUpdatedEvent extends ShipmentEvent {
    private ShipmentStatus oldStatus;
    private ShipmentStatus newStatus;
    private String statusDetails;
    private String location;
} 