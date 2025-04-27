package com.handmade.ecommerce.order.event;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class OrderEventPublisher {
    
    private final ApplicationEventPublisher eventPublisher;
    
    public void publishOrderCreated(OrderCreatedEvent event) {
        enrichEventMetadata(event);
        log.info("Publishing order created event for order: {}", event.getOrderId());
        eventPublisher.publishEvent(event);
    }
    
    public void publishOrderStatusChanged(OrderStatusChangedEvent event) {
        enrichEventMetadata(event);
        log.info("Publishing order status changed event for order: {}", event.getOrderId());
        eventPublisher.publishEvent(event);
    }
    
    private void enrichEventMetadata(OrderEvent event) {
        event.setEventId(UUID.randomUUID().toString());
        event.setTimestamp(LocalDateTime.now());
    }
} 