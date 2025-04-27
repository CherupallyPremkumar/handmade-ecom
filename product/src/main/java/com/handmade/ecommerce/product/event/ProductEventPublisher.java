package com.handmade.ecommerce.product.event;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class ProductEventPublisher {
    
    private final ApplicationEventPublisher eventPublisher;
    
    public void publishStockChanged(ProductStockChangedEvent event) {
        enrichEventMetadata(event);
        log.info("Publishing product stock changed event for product: {}", event.getProductId());
        eventPublisher.publishEvent(event);
    }
    
    public void publishPriceChanged(ProductPriceChangedEvent event) {
        enrichEventMetadata(event);
        log.info("Publishing product price changed event for product: {}", event.getProductId());
        eventPublisher.publishEvent(event);
    }
    
    private void enrichEventMetadata(ProductEvent event) {
        event.setEventId(UUID.randomUUID().toString());
        event.setTimestamp(LocalDateTime.now());
    }
} 