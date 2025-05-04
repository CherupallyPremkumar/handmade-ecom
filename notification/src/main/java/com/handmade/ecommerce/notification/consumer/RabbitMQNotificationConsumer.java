package com.handmade.ecommerce.notification.consumer;

import com.handmade.ecommerce.notification.model.Notification;
import com.handmade.ecommerce.notification.service.NotificationDeliveryService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

/**
 * Consumes notification messages from RabbitMQ queues and processes them.
 */
@Component
@RequiredArgsConstructor
@Slf4j
public class RabbitMQNotificationConsumer {

    private final NotificationDeliveryService deliveryService;

    @RabbitListener(queues = "${handmade.rabbitmq.notification-queue}")
    public void consumeNotification(Notification notification) {
        log.info("Received notification: id={}, type={}, recipient={}", 
                notification.getId(), notification.getNotificationType(), notification.getRecipientId());
        
        try {
            boolean delivered = deliveryService.deliverNotification(notification);
            if (delivered) {
                log.info("Successfully delivered notification: {}", notification.getId());
            } else {
                log.warn("Failed to deliver notification: {}", notification.getId());
                // In a real implementation, you might want to handle retry logic here
                // or move the message to a dead letter queue after several retries
            }
        } catch (Exception e) {
            log.error("Error processing notification: {}", notification.getId(), e);
        }
    }
    
    @RabbitListener(queues = "${handmade.rabbitmq.order-notification-queue}")
    public void consumeOrderEvent(Object orderEvent) {
        log.info("Received order event: {}", orderEvent.getClass().getSimpleName());
        
        // In a real implementation, you would process the order event and convert it to a notification
        // For example, if it's an OrderCreatedEvent, you would create appropriate notifications
        // and send them through the delivery service
        
        log.info("Processing order event: {}", orderEvent);
    }
} 