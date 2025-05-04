package com.handmade.ecommerce.return.listener;

import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Event listener for return-related notification events.
 */
@Component
public class NotificationEventListener {

    private static final Logger log = LoggerFactory.getLogger(NotificationEventListener.class);
    
    private final RabbitTemplate rabbitTemplate;
    
    @Value("${handmade.rabbitmq.notification-exchange}")
    private String notificationExchange;
    
    @Value("${handmade.rabbitmq.notification-routing-key}")
    private String notificationRoutingKey;
    
    public NotificationEventListener(RabbitTemplate rabbitTemplate) {
        this.rabbitTemplate = rabbitTemplate;
    }
    
    /**
     * Listen for notification events from the notification service.
     * 
     * @param message The notification message
     */
    @RabbitListener(queues = "${handmade.rabbitmq.return-notification-queue}")
    public void handleNotificationEvent(String message) {
        log.info("Received notification event in Return service: {}", message);
        // Handle the notification event
    }
    
    /**
     * Send a notification event to the notification service.
     * 
     * @param message The notification message
     */
    public void sendNotificationEvent(String message) {
        log.info("Sending notification event from Return service: {}", message);
        rabbitTemplate.convertAndSend(notificationExchange, notificationRoutingKey, message);
    }
} 