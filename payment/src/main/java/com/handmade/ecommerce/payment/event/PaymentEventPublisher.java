package com.handmade.ecommerce.payment.event;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class PaymentEventPublisher {
    
    private final ApplicationEventPublisher eventPublisher;
    
    public void publishPaymentProcessed(PaymentProcessedEvent event) {
        enrichEventMetadata(event);
        log.info("Publishing payment processed event for order: {}", event.getOrderId());
        eventPublisher.publishEvent(event);
    }
    
    public void publishPaymentRefunded(PaymentRefundedEvent event) {
        enrichEventMetadata(event);
        log.info("Publishing payment refunded event for order: {}", event.getOrderId());
        eventPublisher.publishEvent(event);
    }
    
    private void enrichEventMetadata(PaymentEvent event) {
        event.setEventId(UUID.randomUUID().toString());
        event.setTimestamp(LocalDateTime.now());
    }
} 