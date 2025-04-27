package com.handmade.ecommerce.payment.model;

import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Map;

/**
 * Model for payment responses.
 * Contains all the information returned from a payment gateway.
 */
@Data
public class PaymentResponse {
    private String orderId;
    private String transactionId;
    private String paymentId;
    private BigDecimal amount;
    private String currency;
    private String status; // SUCCESS, FAILED, PENDING, etc.
    private String paymentMethod;
    private String paymentGateway;
    private LocalDateTime timestamp;
    private String errorCode;
    private String errorMessage;
    private Map<String, String> gatewayResponse; // Raw response from the payment gateway
    private Map<String, String> metadata; // Additional metadata
} 