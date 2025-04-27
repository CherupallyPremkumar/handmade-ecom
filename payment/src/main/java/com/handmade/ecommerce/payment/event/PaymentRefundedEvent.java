package com.handmade.ecommerce.payment.event;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.math.BigDecimal;

@Data
@EqualsAndHashCode(callSuper = true)
@SuperBuilder
@NoArgsConstructor
public class PaymentRefundedEvent extends PaymentEvent {
    private String refundId;
    private BigDecimal refundAmount;
    private String refundReason;
    private String refundStatus;
    private String originalTransactionId;
    private Boolean isPartialRefund;
} 