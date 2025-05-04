package com.handmade.ecommerce.shipping.config;

import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Configuration for RabbitMQ in the Shipping service.
 * Sets up exchanges, queues, and bindings for shipping-related events.
 */
@Configuration
public class RabbitMQConfig {

    @Value("${handmade.rabbitmq.shipping-exchange}")
    private String shippingExchange;
    
    @Value("${handmade.rabbitmq.order-exchange}")
    private String orderExchange;
    
    @Value("${handmade.rabbitmq.return-exchange}")
    private String returnExchange;
    
    @Value("${handmade.rabbitmq.notification-exchange}")
    private String notificationExchange;
    
    @Value("${handmade.rabbitmq.shipping-order-queue}")
    private String shippingOrderQueue;
    
    @Value("${handmade.rabbitmq.shipping-return-queue}")
    private String shippingReturnQueue;
    
    @Value("${handmade.rabbitmq.shipping-notification-queue}")
    private String shippingNotificationQueue;
    
    @Value("${handmade.rabbitmq.order-shipping-queue}")
    private String orderShippingQueue;
    
    @Value("${handmade.rabbitmq.return-shipping-queue}")
    private String returnShippingQueue;
    
    @Value("${handmade.rabbitmq.order-created-routing-key}")
    private String orderCreatedRoutingKey;
    
    @Value("${handmade.rabbitmq.order-cancelled-routing-key}")
    private String orderCancelledRoutingKey;
    
    @Value("${handmade.rabbitmq.shipment-created-routing-key}")
    private String shipmentCreatedRoutingKey;
    
    @Value("${handmade.rabbitmq.shipment-updated-routing-key}")
    private String shipmentUpdatedRoutingKey;
    
    @Value("${handmade.rabbitmq.return-shipment-routing-key}")
    private String returnShipmentRoutingKey;
    
    @Value("${handmade.rabbitmq.notification-routing-key}")
    private String notificationRoutingKey;

    // Shipping exchange
    @Bean
    public TopicExchange shippingExchange() {
        return new TopicExchange(shippingExchange);
    }
    
    // External exchanges that we'll receive messages from
    @Bean
    public TopicExchange orderExchange() {
        return new TopicExchange(orderExchange);
    }
    
    @Bean
    public TopicExchange returnExchange() {
        return new TopicExchange(returnExchange);
    }
    
    @Bean
    public DirectExchange notificationExchange() {
        return new DirectExchange(notificationExchange);
    }
    
    // Queues for messages from other services to Shipping service
    @Bean
    public Queue orderShippingQueue() {
        return QueueBuilder.durable(orderShippingQueue)
                .build();
    }
    
    @Bean
    public Queue returnShippingQueue() {
        return QueueBuilder.durable(returnShippingQueue)
                .build();
    }
    
    // Queues for Shipping service messages to other services
    @Bean
    public Queue shippingOrderQueue() {
        return QueueBuilder.durable(shippingOrderQueue)
                .build();
    }
    
    @Bean
    public Queue shippingReturnQueue() {
        return QueueBuilder.durable(shippingReturnQueue)
                .build();
    }
    
    @Bean
    public Queue shippingNotificationQueue() {
        return QueueBuilder.durable(shippingNotificationQueue)
                .build();
    }
    
    // Bindings from Order to Shipping
    @Bean
    public Binding orderCreatedBinding() {
        return BindingBuilder.bind(orderShippingQueue())
                .to(orderExchange())
                .with(orderCreatedRoutingKey);
    }
    
    @Bean
    public Binding orderCancelledBinding() {
        return BindingBuilder.bind(orderShippingQueue())
                .to(orderExchange())
                .with(orderCancelledRoutingKey);
    }
    
    // Bindings from Return to Shipping
    @Bean
    public Binding returnShipmentBinding() {
        return BindingBuilder.bind(returnShippingQueue())
                .to(returnExchange())
                .with(returnShipmentRoutingKey);
    }
    
    // Bindings from Shipping to other services
    @Bean
    public Binding shipmentCreatedBinding() {
        return BindingBuilder.bind(shippingOrderQueue())
                .to(shippingExchange())
                .with(shipmentCreatedRoutingKey);
    }
    
    @Bean
    public Binding shipmentUpdatedBinding() {
        return BindingBuilder.bind(shippingOrderQueue())
                .to(shippingExchange())
                .with(shipmentUpdatedRoutingKey);
    }
    
    // Bindings from Shipping to Notification
    @Bean
    public Binding shipmentCreatedNotificationBinding() {
        return BindingBuilder.bind(shippingNotificationQueue())
                .to(shippingExchange())
                .with(shipmentCreatedRoutingKey);
    }
    
    @Bean
    public Binding shipmentUpdatedNotificationBinding() {
        return BindingBuilder.bind(shippingNotificationQueue())
                .to(shippingExchange())
                .with(shipmentUpdatedRoutingKey);
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