package com.handmade.ecommerce.shipping.event;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class ShipmentEventPublisher {
    
    private final ApplicationEventPublisher eventPublisher;
    
    public void publishShipmentCreated(ShipmentCreatedEvent event) {
        enrichEventMetadata(event);
        log.info("Publishing shipment created event for shipment: {}", event.getShipmentId());
        eventPublisher.publishEvent(event);
    }
    
    public void publishShipmentStatusUpdated(ShipmentStatusUpdatedEvent event) {
        enrichEventMetadata(event);
        log.info("Publishing shipment status updated event for shipment: {}", event.getShipmentId());
        eventPublisher.publishEvent(event);
    }
    
    private void enrichEventMetadata(ShipmentEvent event) {
        event.setEventId(UUID.randomUUID().toString());
        event.setTimestamp(LocalDateTime.now());
    }
} 