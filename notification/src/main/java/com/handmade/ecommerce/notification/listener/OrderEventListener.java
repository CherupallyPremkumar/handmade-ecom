package com.handmade.ecommerce.notification.listener;

import com.handmade.ecommerce.notification.service.NotificationService;
import com.handmade.ecommerce.order.event.OrderCreatedEvent;
import com.handmade.ecommerce.order.event.OrderStatusChangedEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

/**
 * Listens for order-related events and sends appropriate notifications.
 */
@Component
@RequiredArgsConstructor
@Slf4j
public class OrderEventListener {

    private final NotificationService notificationService;

    @EventListener
    public void handleOrderCreated(OrderCreatedEvent event) {
        log.info("Sending order created notification for order: {}", event.getOrderId());
        
        String message = String.format(
            "Thank you for your order! Your order #%s has been received and is being processed. " +
            "Total amount: %s %s. We'll notify you when your order ships.",
            event.getOrderId(),
            event.getTotalAmount(),
            event.getCurrency()
        );
        
        notificationService.sendCustomerNotification(
            event.getCustomerId(),
            "Order Received",
            message,
            "ORDER_CREATED"
        );
        
        // Also notify artisans about new orders for their products
        // This would typically need to know which artisans to notify based on products in the order
        if (event.getItems() != null && !event.getItems().isEmpty()) {
            // In a real implementation, you would determine the artisans from the products
            // For now, we'll just log that we would do this
            log.info("Would notify artisans about new order for their products: {}", event.getOrderId());
        }
        
        // Notify admin about new order
        notificationService.sendAdminNotification(
            "New Order #" + event.getOrderId(),
            "A new order has been placed by customer " + event.getCustomerId() + 
            " for " + event.getTotalAmount() + " " + event.getCurrency(),
            "ORDER_CREATED"
        );
    }

    @EventListener
    public void handleOrderStatusChanged(OrderStatusChangedEvent event) {
        log.info("Sending order status update notification for order: {}", event.getOrderId());
        
        String message = String.format(
            "Your order #%s status has been updated to: %s. %s",
            event.getOrderId(),
            event.getOrderStatus(),
            getOrderStatusDetails(event.getOrderStatus())
        );
        
        notificationService.sendCustomerNotification(
            event.getCustomerId(),
            "Order Status Update",
            message,
            "ORDER_STATUS_UPDATED"
        );
        
        // If the order is delivered, send a follow-up notification asking for review
        if ("DELIVERED".equals(event.getOrderStatus())) {
            sendOrderDeliveredNotification(event);
        }
        
        // If the order is cancelled, send a notification with more details
        if ("CANCELLED".equals(event.getOrderStatus())) {
            sendOrderCancelledNotification(event);
        }
    }
    
    private void sendOrderDeliveredNotification(OrderStatusChangedEvent event) {
        log.info("Sending order delivered follow-up notification for order: {}", event.getOrderId());
        
        String message = String.format(
            "We're happy to confirm your order #%s has been delivered! " +
            "We'd love to hear your thoughts. Please take a moment to review your purchase. " +
            "Thank you for shopping with Handmade!",
            event.getOrderId()
        );
        
        // Send this notification a bit later to give the customer time to receive the package
        // In a real implementation, you would use a scheduler for this
        notificationService.sendCustomerNotification(
            event.getCustomerId(),
            "How was your order?",
            message,
            "ORDER_REVIEW_REQUEST"
        );
    }
    
    private void sendOrderCancelledNotification(OrderStatusChangedEvent event) {
        log.info("Sending order cancelled notification for order: {}", event.getOrderId());
        
        String message = String.format(
            "We're sorry to inform you that your order #%s has been cancelled. " +
            "If you have any questions, please contact our customer support team.",
            event.getOrderId()
        );
        
        notificationService.sendCustomerNotification(
            event.getCustomerId(),
            "Order Cancelled",
            message,
            "ORDER_CANCELLED"
        );
    }
    
    private String getOrderStatusDetails(String status) {
        switch (status) {
            case "CONFIRMED":
                return "Your order has been confirmed and will be processed shortly.";
            case "PROCESSING":
                return "Your order is being processed and prepared for shipping.";
            case "SHIPPED":
                return "Your order has been shipped and is on its way to you.";
            case "DELIVERED":
                return "Your order has been delivered. Enjoy your purchase!";
            case "CANCELLED":
                return "Your order has been cancelled.";
            default:
                return "";
        }
    }
} 