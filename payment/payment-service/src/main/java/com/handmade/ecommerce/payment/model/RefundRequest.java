package com.handmade.ecommerce.payment.model;

import lombok.Data;
import java.math.BigDecimal;
import java.util.Map;

/**
 * Model for refund requests.
 * Contains all the information needed to process a refund.
 */
@Data
public class RefundRequest {
    private String paymentId;
    private String orderId;
    private String transactionId;
    private BigDecimal amount; // If null, refund the full amount
    private String reason;
    private String currency;
    private String paymentGateway;
    private Map<String, String> metadata; // Additional metadata
} 