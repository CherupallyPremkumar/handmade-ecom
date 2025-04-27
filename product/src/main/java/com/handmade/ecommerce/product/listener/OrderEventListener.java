package com.handmade.ecommerce.product.listener;

import com.handmade.ecommerce.order.event.OrderCreatedEvent;
import com.handmade.ecommerce.order.event.OrderStatusChangedEvent;
import com.handmade.ecommerce.product.service.ProductService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@RequiredArgsConstructor
@Slf4j
public class OrderEventListener {

    private final ProductService productService;

    @EventListener
    @Transactional
    public void handleOrderCreated(OrderCreatedEvent event) {
        log.info("Received order created event, updating product stock for order: {}", event.getOrderId());
        
        // Update stock for each ordered item
        event.getItems().forEach(item -> {
            productService.decreaseStock(
                item.getProductId(),
                item.getQuantity(),
                "Order: " + event.getOrderId()
            );
        });
    }

    @EventListener
    @Transactional
    public void handleOrderStatusChanged(OrderStatusChangedEvent event) {
        log.info("Received order status changed event for order: {}", event.getOrderId());
        
        // If order is cancelled, restore stock
        if ("CANCELLED".equals(event.getNewStatus())) {
            productService.restoreStockForCancelledOrder(event.getOrderId());
        }
    }
} 