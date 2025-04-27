package com.handmade.ecommerce.order.listener;

import com.handmade.ecommerce.order.service.OrderService;
import com.handmade.ecommerce.shipping.event.ShipmentCreatedEvent;
import com.handmade.ecommerce.shipping.event.ShipmentStatusUpdatedEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@RequiredArgsConstructor
@Slf4j
public class ShipmentEventListener {

    private final OrderService orderService;

    @EventListener
    @Transactional
    public void handleShipmentCreated(ShipmentCreatedEvent event) {
        log.info("Received shipment created event for order: {}", event.getOrderId());
        
        // Update order with shipping information
        orderService.updateOrderShippingInfo(
            event.getOrderId(),
            event.getTrackingNumber(),
            event.getCarrier(),
            event.getShippingCost(),
            event.getCurrency()
        );
    }

    @EventListener
    @Transactional
    public void handleShipmentStatusUpdated(ShipmentStatusUpdatedEvent event) {
        log.info("Received shipment status updated event for order: {}", event.getOrderId());
        
        // Update order status based on shipment status
        orderService.updateOrderShipmentStatus(
            event.getOrderId(),
            event.getNewStatus(),
            event.getStatusDetails()
        );
    }
} 