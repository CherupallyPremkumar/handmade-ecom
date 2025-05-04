package com.handmade.ecommerce.notification.service;

import com.handmade.ecommerce.notification.model.Notification;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.UUID;

/**
 * Implementation of NotificationService that sends notifications via RabbitMQ.
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class RabbitMQNotificationService implements NotificationService {

    private final RabbitTemplate rabbitTemplate;
    
    @Value("${handmade.rabbitmq.notification-exchange}")
    private String notificationExchange;
    
    @Value("${handmade.rabbitmq.notification-routing-key}")
    private String notificationRoutingKey;

    @Override
    public void sendCustomerNotification(String customerId, String subject, String message, String notificationType) {
        log.info("Sending customer notification, type: {}, customer: {}", notificationType, customerId);
        
        Notification notification = createNotification(
                customerId, 
                "CUSTOMER", 
                subject, 
                message, 
                notificationType);
        
        sendNotification(notification);
    }

    @Override
    public void sendArtisanNotification(String artisanId, String subject, String message, String notificationType) {
        log.info("Sending artisan notification, type: {}, artisan: {}", notificationType, artisanId);
        
        Notification notification = createNotification(
                artisanId, 
                "ARTISAN", 
                subject, 
                message, 
                notificationType);
        
        sendNotification(notification);
    }

    @Override
    public void sendAdminNotification(String subject, String message, String notificationType) {
        log.info("Sending admin notification, type: {}", notificationType);
        
        Notification notification = createNotification(
                "ADMIN", 
                "ADMIN", 
                subject, 
                message, 
                notificationType);
        
        sendNotification(notification);
    }
    
    private Notification createNotification(String recipientId, String recipientType, 
                                           String subject, String message, String notificationType) {
        return Notification.builder()
                .id(UUID.randomUUID().toString())
                .recipientId(recipientId)
                .recipientType(recipientType)
                .subject(subject)
                .message(message)
                .notificationType(notificationType)
                .status("PENDING")
                .timestamp(LocalDateTime.now())
                .build();
    }
    
    private void sendNotification(Notification notification) {
        try {
            rabbitTemplate.convertAndSend(notificationExchange, notificationRoutingKey, notification);
            log.debug("Notification sent to queue: {}", notification);
        } catch (Exception e) {
            log.error("Failed to send notification to queue: {}", notification, e);
            // In a production system, you might want to implement a retry mechanism
            // or store failed notifications in a database for later retry
        }
    }
} 