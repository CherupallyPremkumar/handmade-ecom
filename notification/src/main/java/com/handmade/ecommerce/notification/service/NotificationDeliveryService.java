package com.handmade.ecommerce.notification.service;

import com.handmade.ecommerce.notification.model.Notification;

/**
 * Service for delivering notifications through various channels.
 */
public interface NotificationDeliveryService {

    /**
     * Deliver a notification through the appropriate channel.
     *
     * @param notification the notification to deliver
     * @return true if the notification was successfully delivered, false otherwise
     */
    boolean deliverNotification(Notification notification);
} 