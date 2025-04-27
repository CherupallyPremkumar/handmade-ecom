package com.handmade.ecommerce.payment.model;

import org.chenile.base.model.BaseEntity;
import org.chenile.base.model.Entity;
import lombok.Data;
import lombok.EqualsAndHashCode;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Map;

@Data
@EqualsAndHashCode(callSuper = true)
@Entity
public class Payment extends BaseEntity {
    private String orderId;
    private String userId;
    private BigDecimal amount;
    private String currency;
    private String paymentMethod; // CREDIT_CARD, DEBIT_CARD, UPI, etc.
    private String paymentStatus; // PENDING, SUCCESS, FAILED, REFUNDED
    private String transactionId;
    private String paymentGateway; // STRIPE, PAYPAL, etc.
    private String paymentGatewayResponse;
    private LocalDateTime paymentDate;
    private String errorMessage;
    private String errorCode;
    private String refundReason;
    private LocalDateTime refundDate;
    private String refundId;
    private String billingAddress;
    private String shippingAddress;
    private String customerEmail;
    private String customerName;
    private String customerPhone;
    private String description;
    private Map<String, String> paymentMethodDetails; // Specific details for each payment method
    private Map<String, String> metadata; // Additional metadata

    @Override
    public void prePersist() {
        super.prePersist();
        if (paymentDate == null) {
            paymentDate = LocalDateTime.now();
        }
    }
} 