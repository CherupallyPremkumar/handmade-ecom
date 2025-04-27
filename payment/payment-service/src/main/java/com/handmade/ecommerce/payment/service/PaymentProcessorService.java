package com.handmade.ecommerce.payment.service;

import com.handmade.ecommerce.payment.model.Payment;
import com.handmade.ecommerce.payment.model.PaymentRequest;
import com.handmade.ecommerce.payment.model.PaymentResponse;
import com.handmade.ecommerce.payment.model.RefundRequest;
import com.handmade.ecommerce.payment.model.RefundResponse;
import com.handmade.ecommerce.payment.gateway.PaymentGateway;
import java.util.List;

/**
 * Service for processing payments through different payment gateways.
 */
public interface PaymentProcessorService {
    
    /**
     * Process a payment
     * @param paymentRequest the payment request
     * @return the payment response
     */
    PaymentResponse processPayment(PaymentRequest paymentRequest);
    
    /**
     * Process a refund
     * @param refundRequest the refund request
     * @return the refund response
     */
    RefundResponse processRefund(RefundRequest refundRequest);
    
    /**
     * Get the payment processor for a specific payment method
     * @param paymentMethod the payment method
     * @return the payment processor
     */
    PaymentGateway getPaymentGateway(String paymentMethod);
    
    /**
     * Get all supported payment methods
     * @return the list of supported payment methods
     */
    List<String> getSupportedPaymentMethods();
    
    /**
     * Check if a payment method is supported
     * @param paymentMethod the payment method to check
     * @return true if the payment method is supported, false otherwise
     */
    boolean isPaymentMethodSupported(String paymentMethod);
} 