package com.handmade.ecommerce.order.listener;

import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * Event listener for order-related notification events.
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class NotificationEventListener {

    private final RabbitTemplate rabbitTemplate;

    @Value("${handmade.rabbitmq.notification-exchange}")
    private String notificationExchange;

    @Value("${handmade.rabbitmq.notification-routing-key}")
    private String notificationRoutingKey;

    /**
     * Listen for notification events from the notification service.
     * 
     * @param message The notification message
     */
    @RabbitListener(queues = "${handmade.rabbitmq.order-notification-queue}")
    public void handleNotificationEvent(String message) {
        log.info("Received notification event in Order service: {}", message);
        // Handle the notification event
    }

    /**
     * Send a notification event to the notification service.
     * 
     * @param message The notification message
     */
    public void sendNotificationEvent(String message) {
        log.info("Sending notification event from Order service: {}", message);
        rabbitTemplate.convertAndSend(notificationExchange, notificationRoutingKey, message);
    }
}