package com.handmade.ecommerce.notification.controller;

import com.handmade.ecommerce.notification.model.Notification;
import com.handmade.ecommerce.notification.service.NotificationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * Controller for testing notification functionality.
 * In a production environment, this would be limited to admin access.
 */
@RestController
@RequestMapping("/api/notifications")
@RequiredArgsConstructor
@Slf4j
public class NotificationController {

    private final NotificationService notificationService;

    @PostMapping("/test/customer/{customerId}")
    public ResponseEntity<String> sendTestCustomerNotification(
            @PathVariable String customerId,
            @RequestParam(defaultValue = "Test Notification") String subject,
            @RequestParam(defaultValue = "This is a test notification for the customer.") String message,
            @RequestParam(defaultValue = "TEST") String notificationType) {
        
        log.info("Sending test notification to customer: {}", customerId);
        
        notificationService.sendCustomerNotification(
            customerId,
            subject,
            message,
            notificationType
        );
        
        return ResponseEntity.ok("Test notification sent to customer: " + customerId);
    }

    @PostMapping("/test/artisan/{artisanId}")
    public ResponseEntity<String> sendTestArtisanNotification(
            @PathVariable String artisanId,
            @RequestParam(defaultValue = "Test Notification") String subject,
            @RequestParam(defaultValue = "This is a test notification for the artisan.") String message,
            @RequestParam(defaultValue = "TEST") String notificationType) {
        
        log.info("Sending test notification to artisan: {}", artisanId);
        
        notificationService.sendArtisanNotification(
            artisanId,
            subject,
            message,
            notificationType
        );
        
        return ResponseEntity.ok("Test notification sent to artisan: " + artisanId);
    }

    @PostMapping("/test/admin")
    public ResponseEntity<String> sendTestAdminNotification(
            @RequestParam(defaultValue = "Test Admin Notification") String subject,
            @RequestParam(defaultValue = "This is a test notification for the admin.") String message,
            @RequestParam(defaultValue = "TEST") String notificationType) {
        
        log.info("Sending test notification to admin");
        
        notificationService.sendAdminNotification(
            subject,
            message,
            notificationType
        );
        
        return ResponseEntity.ok("Test notification sent to admin");
    }
    
    @PostMapping("/test/order-notification")
    public ResponseEntity<String> simulateOrderNotification(
            @RequestParam String customerId,
            @RequestParam String orderId,
            @RequestParam String orderStatus) {
        
        log.info("Simulating order notification for order: {}, status: {}", orderId, orderStatus);
        
        String message;
        String subject;
        String type;
        
        if ("CREATED".equals(orderStatus)) {
            subject = "Order Received";
            message = "Thank you for your order! Your order #" + orderId + " has been received and is being processed.";
            type = "ORDER_CREATED";
        } else if ("DELIVERED".equals(orderStatus)) {
            subject = "Order Delivered";
            message = "Your order #" + orderId + " has been delivered. Enjoy your purchase!";
            type = "ORDER_DELIVERED";
        } else {
            subject = "Order Status Update";
            message = "Your order #" + orderId + " status has been updated to: " + orderStatus;
            type = "ORDER_STATUS_UPDATED";
        }
        
        notificationService.sendCustomerNotification(
            customerId,
            subject,
            message,
            type
        );
        
        return ResponseEntity.ok("Order notification sent for order: " + orderId);
    }
} 