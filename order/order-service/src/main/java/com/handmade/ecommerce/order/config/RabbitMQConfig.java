package com.handmade.ecommerce.order.config;

import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Configuration for RabbitMQ in the Order service.
 * Sets up exchanges, queues, and bindings for order-related events.
 */
@Configuration
public class RabbitMQConfig {

    @Value("${handmade.rabbitmq.order-exchange}")
    private String orderExchange;

    @Value("${handmade.rabbitmq.shipping-exchange}")
    private String shippingExchange;

    @Value("${handmade.rabbitmq.return-exchange}")
    private String returnExchange;

    @Value("${handmade.rabbitmq.notification-exchange}")
    private String notificationExchange;

    @Value("${handmade.rabbitmq.order-created-queue}")
    private String orderCreatedQueue;

    @Value("${handmade.rabbitmq.order-cancelled-queue}")
    private String orderCancelledQueue;

    @Value("${handmade.rabbitmq.order-shipping-queue}")
    private String orderShippingQueue;

    @Value("${handmade.rabbitmq.order-return-queue}")
    private String orderReturnQueue;

    @Value("${handmade.rabbitmq.order-notification-queue}")
    private String orderNotificationQueue;

    @Value("${handmade.rabbitmq.shipping-order-queue}")
    private String shippingOrderQueue;

    @Value("${handmade.rabbitmq.return-order-queue}")
    private String returnOrderQueue;

    @Value("${handmade.rabbitmq.order-created-routing-key}")
    private String orderCreatedRoutingKey;

    @Value("${handmade.rabbitmq.order-cancelled-routing-key}")
    private String orderCancelledRoutingKey;

    @Value("${handmade.rabbitmq.order-delivered-routing-key}")
    private String orderDeliveredRoutingKey;

    @Value("${handmade.rabbitmq.order-updated-routing-key}")
    private String orderUpdatedRoutingKey;

    @Value("${handmade.rabbitmq.shipment-created-routing-key}")
    private String shipmentCreatedRoutingKey;

    @Value("${handmade.rabbitmq.shipment-updated-routing-key}")
    private String shipmentUpdatedRoutingKey;

    @Value("${handmade.rabbitmq.return-requested-routing-key}")
    private String returnRequestedRoutingKey;

    @Value("${handmade.rabbitmq.return-completed-routing-key}")
    private String returnCompletedRoutingKey;

    @Value("${handmade.rabbitmq.notification-routing-key}")
    private String notificationRoutingKey;

    // Order exchange
    @Bean
    public TopicExchange orderExchange() {
        return new TopicExchange(orderExchange);
    }

    // External exchanges that we'll receive messages from
    @Bean
    public TopicExchange shippingExchange() {
        return new TopicExchange(shippingExchange);
    }

    @Bean
    public TopicExchange returnExchange() {
        return new TopicExchange(returnExchange);
    }

    @Bean
    public DirectExchange notificationExchange() {
        return new DirectExchange(notificationExchange);
    }

    // Queues for messages from other services to Order service
    @Bean
    public Queue shippingOrderQueue() {
        return QueueBuilder.durable(shippingOrderQueue)
                .build();
    }

    @Bean
    public Queue returnOrderQueue() {
        return QueueBuilder.durable(returnOrderQueue)
                .build();
    }

    // Queues for Order service messages to other services
    @Bean
    public Queue orderShippingQueue() {
        return QueueBuilder.durable(orderShippingQueue)
                .build();
    }

    @Bean
    public Queue orderReturnQueue() {
        return QueueBuilder.durable(orderReturnQueue)
                .build();
    }

    @Bean
    public Queue orderNotificationQueue() {
        return QueueBuilder.durable(orderNotificationQueue)
                .build();
    }

    // Bindings from Shipping to Order
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

    // Bindings from Order to other services
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

    @Bean
    public Binding orderDeliveredBinding() {
        return BindingBuilder.bind(orderReturnQueue())
                .to(orderExchange())
                .with(orderDeliveredRoutingKey);
    }

    // Bindings from Order to Notification
    @Bean
    public Binding orderCreatedNotificationBinding() {
        return BindingBuilder.bind(orderNotificationQueue())
                .to(orderExchange())
                .with(orderCreatedRoutingKey);
    }

    @Bean
    public Binding orderUpdatedNotificationBinding() {
        return BindingBuilder.bind(orderNotificationQueue())
                .to(orderExchange())
                .with(orderUpdatedRoutingKey);
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