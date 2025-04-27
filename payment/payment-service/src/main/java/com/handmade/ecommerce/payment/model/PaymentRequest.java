package com.handmade.ecommerce.payment.model;

import lombok.Data;
import java.math.BigDecimal;
import java.util.Map;

/**
 * Model for payment requests.
 * Contains all the information needed to process a payment.
 */
@Data
public class PaymentRequest {
    private String orderId;
    private String userId;
    private BigDecimal amount;
    private String currency;
    private String paymentMethod;
    private String paymentGateway;
    private String description;
    private String customerEmail;
    private String customerName;
    private String customerPhone;
    private String billingAddress;
    private String shippingAddress;
    private Map<String, String> paymentMethodDetails; // Specific details for each payment method
    private Map<String, String> metadata; // Additional metadata
} 