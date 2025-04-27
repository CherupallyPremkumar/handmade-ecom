package com.handmade.ecommerce.payment.api.dto;

import lombok.Data;

@Data
public class PaymentDTO {
    private String id;
    private String orderId;
    private String userId;
    private double amount;
    private String currency;
    private String paymentMethod;
    private String paymentStatus;
    private String transactionId;
    private String paymentGateway;
    private String paymentGatewayResponse;
    private String paymentDate;
    private String errorMessage;
    private String refundReason;
    private String refundDate;
    private String billingAddress;
    private String shippingAddress;
} 