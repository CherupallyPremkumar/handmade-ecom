package com.handmade.ecommerce.payment.model;

import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Map;

/**
 * Model for refund responses.
 * Contains all the information returned from a payment gateway for a refund.
 */
@Data
public class RefundResponse {
    private String paymentId;
    private String orderId;
    private String transactionId;
    private String refundId;
    private BigDecimal amount;
    private String currency;
    private String status; // SUCCESS, FAILED, PENDING, etc.
    private String reason;
    private String paymentGateway;
    private LocalDateTime timestamp;
    private String errorCode;
    private String errorMessage;
    private Map<String, String> gatewayResponse; // Raw response from the payment gateway
    private Map<String, String> metadata; // Additional metadata
} 