package com.handmade.ecommerce.notification.service;

/**
 * Service for sending notifications to customers and other system users.
 */
public interface NotificationService {
    
    /**
     * Send a notification to a customer.
     *
     * @param customerId    The ID of the customer
     * @param subject       The notification subject
     * @param message       The notification message
     * @param notificationType The type of notification (e.g., ORDER_CREATED, SHIPPING_UPDATE)
     */
    void sendCustomerNotification(String customerId, String subject, String message, String notificationType);
    
    /**
     * Send a notification to an artisan.
     *
     * @param artisanId     The ID of the artisan
     * @param subject       The notification subject
     * @param message       The notification message
     * @param notificationType The type of notification
     */
    void sendArtisanNotification(String artisanId, String subject, String message, String notificationType);
    
    /**
     * Send a notification to an administrator.
     *
     * @param subject       The notification subject
     * @param message       The notification message
     * @param notificationType The type of notification
     */
    void sendAdminNotification(String subject, String message, String notificationType);
} 