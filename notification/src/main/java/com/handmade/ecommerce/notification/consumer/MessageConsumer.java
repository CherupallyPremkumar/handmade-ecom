package com.handmade.ecommerce.notification.consumer;

import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.handmade.ecommerce.notification.service.NotificationService;

/**
 * Consumes messages from RabbitMQ queues and processes them.
 */
@Component
public class MessageConsumer {

    private static final Logger log = LoggerFactory.getLogger(MessageConsumer.class);
    
    private final NotificationService notificationService;
    
    public MessageConsumer(NotificationService notificationService) {
        this.notificationService = notificationService;
    }
    
    /**
     * Listen for order events that require notifications.
     * 
     * @param message The order event message
     */
    @RabbitListener(queues = "${handmade.rabbitmq.order-notification-queue}")
    public void handleOrderMessage(String message) {
        log.info("Received order notification message: {}", message);
        notificationService.sendNotification("Order", message);
    }
    
    /**
     * Listen for shipping events that require notifications.
     * 
     * @param message The shipping event message
     */
    @RabbitListener(queues = "${handmade.rabbitmq.shipping-notification-queue}")
    public void handleShippingMessage(String message) {
        log.info("Received shipping notification message: {}", message);
        notificationService.sendNotification("Shipping", message);
    }
    
    /**
     * Listen for return events that require notifications.
     * 
     * @param message The return event message
     */
    @RabbitListener(queues = "${handmade.rabbitmq.return-notification-queue}")
    public void handleReturnMessage(String message) {
        log.info("Received return notification message: {}", message);
        notificationService.sendNotification("Return", message);
    }
} 