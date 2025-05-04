package com.handmade.ecommerce.notification.config;

import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Configuration for RabbitMQ exchanges, queues, and bindings.
 */
@Configuration
public class RabbitMQConfig {

    @Value("${handmade.rabbitmq.notification-exchange}")
    private String notificationExchange;
    
    @Value("${handmade.rabbitmq.notification-queue}")
    private String notificationQueue;
    
    @Value("${handmade.rabbitmq.notification-routing-key}")
    private String notificationRoutingKey;
    
    @Value("${handmade.rabbitmq.order-exchange}")
    private String orderExchange;
    
    @Value("${handmade.rabbitmq.order-notification-queue}")
    private String orderNotificationQueue;
    
    @Value("${handmade.rabbitmq.order-created-routing-key}")
    private String orderCreatedRoutingKey;
    
    @Value("${handmade.rabbitmq.order-updated-routing-key}")
    private String orderUpdatedRoutingKey;
    
    @Value("${handmade.rabbitmq.shipping-exchange}")
    private String shippingExchange;
    
    @Value("${handmade.rabbitmq.shipping-notification-queue}")
    private String shippingNotificationQueue;
    
    @Value("${handmade.rabbitmq.shipment-created-routing-key}")
    private String shipmentCreatedRoutingKey;
    
    @Value("${handmade.rabbitmq.shipment-updated-routing-key}")
    private String shipmentUpdatedRoutingKey;
    
    @Value("${handmade.rabbitmq.return-exchange}")
    private String returnExchange;
    
    @Value("${handmade.rabbitmq.return-notification-queue}")
    private String returnNotificationQueue;
    
    @Value("${handmade.rabbitmq.return-requested-routing-key}")
    private String returnRequestedRoutingKey;
    
    @Value("${handmade.rabbitmq.return-completed-routing-key}")
    private String returnCompletedRoutingKey;

    // Notification exchange and queue
    @Bean
    public DirectExchange notificationExchange() {
        return new DirectExchange(notificationExchange);
    }
    
    @Bean
    public Queue notificationQueue() {
        return QueueBuilder.durable(notificationQueue)
                .build();
    }
    
    @Bean
    public Binding notificationBinding() {
        return BindingBuilder.bind(notificationQueue())
                .to(notificationExchange())
                .with(notificationRoutingKey);
    }
    
    // Order exchange and notification queue
    @Bean
    public TopicExchange orderExchange() {
        return new TopicExchange(orderExchange);
    }
    
    @Bean
    public Queue orderNotificationQueue() {
        return QueueBuilder.durable(orderNotificationQueue)
                .build();
    }
    
    @Bean
    public Binding orderCreatedBinding() {
        return BindingBuilder.bind(orderNotificationQueue())
                .to(orderExchange())
                .with(orderCreatedRoutingKey);
    }
    
    @Bean
    public Binding orderUpdatedBinding() {
        return BindingBuilder.bind(orderNotificationQueue())
                .to(orderExchange())
                .with(orderUpdatedRoutingKey);
    }
    
    // Shipping exchange and notification queue
    @Bean
    public TopicExchange shippingExchange() {
        return new TopicExchange(shippingExchange);
    }
    
    @Bean
    public Queue shippingNotificationQueue() {
        return QueueBuilder.durable(shippingNotificationQueue)
                .build();
    }
    
    @Bean
    public Binding shipmentCreatedBinding() {
        return BindingBuilder.bind(shippingNotificationQueue())
                .to(shippingExchange())
                .with(shipmentCreatedRoutingKey);
    }
    
    @Bean
    public Binding shipmentUpdatedBinding() {
        return BindingBuilder.bind(shippingNotificationQueue())
                .to(shippingExchange())
                .with(shipmentUpdatedRoutingKey);
    }
    
    // Return exchange and notification queue
    @Bean
    public TopicExchange returnExchange() {
        return new TopicExchange(returnExchange);
    }
    
    @Bean
    public Queue returnNotificationQueue() {
        return QueueBuilder.durable(returnNotificationQueue)
                .build();
    }
    
    @Bean
    public Binding returnRequestedBinding() {
        return BindingBuilder.bind(returnNotificationQueue())
                .to(returnExchange())
                .with(returnRequestedRoutingKey);
    }
    
    @Bean
    public Binding returnCompletedBinding() {
        return BindingBuilder.bind(returnNotificationQueue())
                .to(returnExchange())
                .with(returnCompletedRoutingKey);
    }
    
    // Message converter
    @Bean
    public MessageConverter jsonMessageConverter() {
        return new Jackson2JsonMessageConverter();
    }
    
    @Bean
    public RabbitTemplate rabbitTemplate(ConnectionFactory connectionFactory) {
        RabbitTemplate rabbitTemplate = new RabbitTemplate(connectionFactory);
        rabbitTemplate.setMessageConverter(jsonMessageConverter());
        return rabbitTemplate;
    }
} 