package com.handmade.ecommerce.payment.listener;

import com.handmade.ecommerce.order.event.OrderCreatedEvent;
import com.handmade.ecommerce.order.event.OrderStatusChangedEvent;
import com.handmade.ecommerce.payment.service.PaymentService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@RequiredArgsConstructor
@Slf4j
public class OrderEventListener {

    private final PaymentService paymentService;

    @EventListener
    @Transactional
    public void handleOrderCreated(OrderCreatedEvent event) {
        log.info("Received order created event, initiating payment for order: {}", event.getOrderId());
        
        // Initiate payment processing
        paymentService.processPayment(
            event.getOrderId(),
            event.getCustomerId(),
            event.getTotalAmount(),
            event.getCurrency(),
            event.getPaymentMethod()
        );
    }

    @EventListener
    @Transactional
    public void handleOrderStatusChanged(OrderStatusChangedEvent event) {
        log.info("Received order status changed event for order: {}", event.getOrderId());
        
        // If order is cancelled and already paid, initiate refund
        if ("CANCELLED".equals(event.getNewStatus())) {
            paymentService.initiateRefundForCancelledOrder(
                event.getOrderId(),
                event.getReason()
            );
        }
    }
} 