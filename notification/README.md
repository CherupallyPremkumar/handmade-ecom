# Notification Service

This module handles sending notifications to customers, artisans, and administrators based on various events in the Handmade E-commerce platform.

## Features

- **Event-Based Notifications**: Automatically sends notifications based on system events
- **Multi-Channel Delivery**: Supports email, SMS, and push notifications
- **RabbitMQ Integration**: Uses RabbitMQ for reliable message delivery
- **Customizable Templates**: Notification messages can be customized
- **Delivery Status Tracking**: Tracks the status of notification delivery

## Key Components

### Services

- `NotificationService`: Interface for sending notifications
- `RabbitMQNotificationService`: Sends notifications via RabbitMQ
- `NotificationDeliveryService`: Delivers notifications via different channels
- `DefaultNotificationDeliveryService`: Default implementation that simulates delivery

### Event Listeners

- `OrderEventListener`: Listens for order-related events:
  - Order creation
  - Order status changes
  - Order delivery
  - Order cancellation
- `ShipmentEventListener`: Listens for shipment-related events:
  - Shipment creation
  - Shipment status updates

### RabbitMQ Components

- `RabbitMQConfig`: RabbitMQ exchange and queue configuration
- `RabbitMQNotificationConsumer`: Consumes notification messages from RabbitMQ

## Testing

A test controller is included for manual testing:

```
POST /api/notifications/test/customer/{customerId}
POST /api/notifications/test/artisan/{artisanId}
POST /api/notifications/test/admin
POST /api/notifications/test/order-notification?customerId=123&orderId=456&orderStatus=DELIVERED
```

## Configuration

Configure RabbitMQ connection and notification settings in `application.yml`:

```yaml
spring:
  rabbitmq:
    host: localhost
    port: 5672
    username: handmade
    password: handmade
    virtual-host: handmade_vhost

handmade:
  rabbitmq:
    notification-exchange: handmade.notification.exchange
    notification-queue: handmade.notification.queue
    notification-routing-key: notification.delivery

    order-exchange: handmade.order.exchange
    order-notification-queue: handmade.order.notification.queue
    order-created-routing-key: order.created
    order-updated-routing-key: order.status.updated
```
