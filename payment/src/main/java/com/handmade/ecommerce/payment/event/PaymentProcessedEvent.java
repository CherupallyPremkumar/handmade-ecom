package com.handmade.ecommerce.payment.event;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Data
@EqualsAndHashCode(callSuper = true)
@SuperBuilder
@NoArgsConstructor
public class PaymentProcessedEvent extends PaymentEvent {
    private String status;
    private String transactionId;
    private String processorResponse;
    private String cardLast4;
    private String cardBrand;
    private Boolean isSuccessful;
    private String failureReason;
} 