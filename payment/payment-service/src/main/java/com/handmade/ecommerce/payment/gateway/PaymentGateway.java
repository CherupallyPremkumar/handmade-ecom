package com.handmade.ecommerce.payment.gateway;

import com.handmade.ecommerce.payment.model.Payment;
import com.handmade.ecommerce.payment.model.PaymentRequest;
import com.handmade.ecommerce.payment.model.PaymentResponse;
import com.handmade.ecommerce.payment.model.RefundRequest;
import com.handmade.ecommerce.payment.model.RefundResponse;
import java.util.List;

/**
 * Interface for payment gateway implementations.
 * All payment gateways must implement this interface.
 */
public interface PaymentGateway {
    
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
     * Get the name of the payment gateway
     * @return the name of the payment gateway
     */
    String getGatewayName();
    
    /**
     * Check if the payment gateway supports a specific payment method
     * @param paymentMethod the payment method to check
     * @return true if the payment gateway supports the payment method, false otherwise
     */
    boolean supportsPaymentMethod(String paymentMethod);
    
    /**
     * Get all payment methods supported by this gateway
     * @return the list of supported payment methods
     */
    List<String> getSupportedPaymentMethods();
} 