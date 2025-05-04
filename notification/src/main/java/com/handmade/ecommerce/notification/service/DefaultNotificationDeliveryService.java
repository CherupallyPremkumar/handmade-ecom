package com.handmade.ecommerce.notification.service;

import com.handmade.ecommerce.notification.model.Notification;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Default implementation of the NotificationDeliveryService.
 * In a real application, this would send notifications via email, SMS, push notifications, etc.
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class DefaultNotificationDeliveryService implements NotificationDeliveryService {

    // For demonstration purposes, we'll use an in-memory store
    // In a real application, you would use a database and external services
    private final Map<String, Notification> sentNotifications = new ConcurrentHashMap<>();
    
    // In a real application, inject services for different notification channels
    // private final EmailService emailService;
    // private final SmsService smsService;
    // private final PushNotificationService pushService;

    @Override
    public boolean deliverNotification(Notification notification) {
        log.info("Delivering notification: {}", notification);
        
        // Determine the appropriate delivery channel based on the recipient and notification type
        String channel = determineDeliveryChannel(notification);
        
        // Send the notification via the chosen channel
        boolean delivered = sendNotificationViaChannel(notification, channel);
        
        if (delivered) {
            // Update notification status and timestamp
            notification.setStatus("SENT");
            notification.setSentTimestamp(LocalDateTime.now());
            
            // Store the sent notification (in a real app, this would be in a database)
            sentNotifications.put(notification.getId(), notification);
        } else {
            notification.setStatus("FAILED");
            
            // Update retry count
            Integer retryCount = notification.getRetryCount();
            notification.setRetryCount((retryCount == null) ? 1 : retryCount + 1);
        }
        
        return delivered;
    }
    
    private String determineDeliveryChannel(Notification notification) {
        // Logic to determine the best channel for this notification
        // This could be based on user preferences, notification type, etc.
        
        // For demonstration purposes, we'll use a simple mapping
        if ("CUSTOMER".equals(notification.getRecipientType())) {
            if (notification.getNotificationType().contains("ORDER")) {
                return "EMAIL";
            } else if (notification.getNotificationType().contains("SHIPPING")) {
                return "EMAIL+SMS";
            }
        } else if ("ARTISAN".equals(notification.getRecipientType())) {
            return "EMAIL";
        } else if ("ADMIN".equals(notification.getRecipientType())) {
            return "EMAIL";
        }
        
        // Default to email
        return "EMAIL";
    }
    
    private boolean sendNotificationViaChannel(Notification notification, String channel) {
        log.info("Sending notification {} via channel: {}", notification.getId(), channel);
        
        try {
            // In a real implementation, this would call the actual notification services
            if (channel.contains("EMAIL")) {
                // emailService.sendEmail(...);
                log.info("EMAIL would be sent to: {}", notification.getRecipientId());
            }
            
            if (channel.contains("SMS")) {
                // smsService.sendSms(...);
                log.info("SMS would be sent to: {}", notification.getRecipientId());
            }
            
            if (channel.contains("PUSH")) {
                // pushService.sendPushNotification(...);
                log.info("PUSH notification would be sent to: {}", notification.getRecipientId());
            }
            
            // For demonstration, we'll just simulate success
            return true;
        } catch (Exception e) {
            log.error("Failed to send notification via channel {}: {}", channel, e.getMessage());
            return false;
        }
    }
} 