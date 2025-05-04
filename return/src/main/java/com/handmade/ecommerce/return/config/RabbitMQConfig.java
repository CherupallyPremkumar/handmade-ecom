package com.handmade.ecommerce.return.config;

import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Configuration for RabbitMQ in the Return service.
 * Sets up exchanges, queues, and bindings for return-related events.
 */
@Configuration
public class RabbitMQConfig {

    @Value("${handmade.rabbitmq.return-exchange}")
    private String returnExchange;
    
    @Value("${handmade.rabbitmq.order-exchange}")
    private String orderExchange;
    
    @Value("${handmade.rabbitmq.shipping-exchange}")
    private String shippingExchange;
    
    @Value("${handmade.rabbitmq.notification-exchange}")
    private String notificationExchange;
    
    @Value("${handmade.rabbitmq.return-order-queue}")
    private String returnOrderQueue;
    
    @Value("${handmade.rabbitmq.return-shipping-queue}")
    private String returnShippingQueue;
    
    @Value("${handmade.rabbitmq.return-notification-queue}")
    private String returnNotificationQueue;
    
    @Value("${handmade.rabbitmq.order-return-queue}")
    private String orderReturnQueue;
    
    @Value("${handmade.rabbitmq.shipping-return-queue}")
    private String shippingReturnQueue;
    
    @Value("${handmade.rabbitmq.order-delivered-routing-key}")
    private String orderDeliveredRoutingKey;
    
    @Value("${handmade.rabbitmq.return-requested-routing-key}")
    private String returnRequestedRoutingKey;
    
    @Value("${handmade.rabbitmq.return-completed-routing-key}")
    private String returnCompletedRoutingKey;
    
    @Value("${handmade.rabbitmq.return-shipment-routing-key}")
    private String returnShipmentRoutingKey;
    
    @Value("${handmade.rabbitmq.notification-routing-key}")
    private String notificationRoutingKey;

    // Return exchange
    @Bean
    public TopicExchange returnExchange() {
        return new TopicExchange(returnExchange);
    }
    
    // External exchanges that we'll receive messages from
    @Bean
    public TopicExchange orderExchange() {
        return new TopicExchange(orderExchange);
    }
    
    @Bean
    public TopicExchange shippingExchange() {
        return new TopicExchange(shippingExchange);
    }
    
    @Bean
    public DirectExchange notificationExchange() {
        return new DirectExchange(notificationExchange);
    }
    
    // Queues for messages from other services to Return service
    @Bean
    public Queue orderReturnQueue() {
        return QueueBuilder.durable(orderReturnQueue)
                .build();
    }
    
    @Bean
    public Queue shippingReturnQueue() {
        return QueueBuilder.durable(shippingReturnQueue)
                .build();
    }
    
    // Queues for Return service messages to other services
    @Bean
    public Queue returnOrderQueue() {
        return QueueBuilder.durable(returnOrderQueue)
                .build();
    }
    
    @Bean
    public Queue returnShippingQueue() {
        return QueueBuilder.durable(returnShippingQueue)
                .build();
    }
    
    @Bean
    public Queue returnNotificationQueue() {
        return QueueBuilder.durable(returnNotificationQueue)
                .build();
    }
    
    // Bindings from Order to Return
    @Bean
    public Binding orderDeliveredBinding() {
        return BindingBuilder.bind(orderReturnQueue())
                .to(orderExchange())
                .with(orderDeliveredRoutingKey);
    }
    
    // Bindings from Return to Order
    @Bean
    public Binding returnRequestedBinding() {
        return BindingBuilder.bind(returnOrderQueue())
                .to(returnExchange())
                .with(returnRequestedRoutingKey);
    }
    
    @Bean
    public Binding returnCompletedBinding() {
        return BindingBuilder.bind(returnOrderQueue())
                .to(returnExchange())
                .with(returnCompletedRoutingKey);
    }
    
    // Bindings from Return to Shipping
    @Bean
    public Binding returnShipmentBinding() {
        return BindingBuilder.bind(returnShippingQueue())
                .to(returnExchange())
                .with(returnShipmentRoutingKey);
    }
    
    // Bindings from Return to Notification
    @Bean
    public Binding returnRequestedNotificationBinding() {
        return BindingBuilder.bind(returnNotificationQueue())
                .to(returnExchange())
                .with(returnRequestedRoutingKey);
    }
    
    @Bean
    public Binding returnCompletedNotificationBinding() {
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