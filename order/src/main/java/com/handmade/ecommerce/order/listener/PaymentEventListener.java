package com.handmade.ecommerce.order.listener;

import com.handmade.ecommerce.order.service.OrderService;
import com.handmade.ecommerce.payment.event.PaymentProcessedEvent;
import com.handmade.ecommerce.payment.event.PaymentRefundedEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@RequiredArgsConstructor
@Slf4j
public class PaymentEventListener {

    private final OrderService orderService;

    @EventListener
    @Transactional
    public void handlePaymentProcessed(PaymentProcessedEvent event) {
        log.info("Received payment processed event for order: {}", event.getOrderId());
        
        if (event.getIsSuccessful()) {
            orderService.updateOrderPaymentStatus(
                event.getOrderId(),
                "PAID",
                event.getTransactionId()
            );
        } else {
            orderService.updateOrderPaymentStatus(
                event.getOrderId(),
                "PAYMENT_FAILED",
                event.getFailureReason()
            );
        }
    }

    @EventListener
    @Transactional
    public void handlePaymentRefunded(PaymentRefundedEvent event) {
        log.info("Received payment refunded event for order: {}", event.getOrderId());
        
        orderService.updateOrderRefundStatus(
            event.getOrderId(),
            event.getRefundId(),
            event.getRefundAmount(),
            event.getIsPartialRefund()
        );
    }
} 