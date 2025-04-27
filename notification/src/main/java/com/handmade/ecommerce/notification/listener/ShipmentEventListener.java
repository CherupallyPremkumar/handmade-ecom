package com.handmade.ecommerce.notification.listener;

import com.handmade.ecommerce.notification.service.NotificationService;
import com.handmade.ecommerce.shipping.event.ShipmentCreatedEvent;
import com.handmade.ecommerce.shipping.event.ShipmentStatusUpdatedEvent;
import com.handmade.ecommerce.shipping.model.ShipmentStatus;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class ShipmentEventListener {

    private final NotificationService notificationService;

    @EventListener
    public void handleShipmentCreated(ShipmentCreatedEvent event) {
        log.info("Sending shipment created notification for order: {}", event.getOrderId());
        
        String message = String.format(
            "Your order %s has been shipped via %s. Tracking number: %s",
            event.getOrderId(),
            event.getCarrier(),
            event.getTrackingNumber()
        );
        
        notificationService.sendCustomerNotification(
            event.getCustomerId(),
            "Order Shipped",
            message,
            "SHIPPING_UPDATE"
        );
    }

    @EventListener
    public void handleShipmentStatusUpdated(ShipmentStatusUpdatedEvent event) {
        log.info("Sending shipment status update notification for order: {}", event.getOrderId());
        
        // Only send notifications for significant status changes
        if (isSignificantStatusChange(event.getOldStatus(), event.getNewStatus())) {
            String message = String.format(
                "Your shipment for order %s has been updated. Status: %s. %s",
                event.getOrderId(),
                event.getNewStatus(),
                event.getStatusDetails()
            );
            
            notificationService.sendCustomerNotification(
                event.getCustomerId(),
                "Shipment Update",
                message,
                "SHIPPING_UPDATE"
            );
        }
    }
    
    private boolean isSignificantStatusChange(ShipmentStatus oldStatus, ShipmentStatus newStatus) {
        // Define which status changes should trigger notifications
        return newStatus == ShipmentStatus.OUT_FOR_DELIVERY ||
               newStatus == ShipmentStatus.DELIVERED ||
               newStatus == ShipmentStatus.EXCEPTION ||
               newStatus == ShipmentStatus.CANCELLED;
    }
} 