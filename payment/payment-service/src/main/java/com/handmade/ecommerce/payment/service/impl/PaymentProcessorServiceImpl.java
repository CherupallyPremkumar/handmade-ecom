package com.handmade.ecommerce.payment.service.impl;

import com.handmade.ecommerce.payment.gateway.PaymentGateway;
import com.handmade.ecommerce.payment.model.PaymentRequest;
import com.handmade.ecommerce.payment.model.PaymentResponse;
import com.handmade.ecommerce.payment.model.RefundRequest;
import com.handmade.ecommerce.payment.model.RefundResponse;
import com.handmade.ecommerce.payment.service.PaymentProcessorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Implementation of the PaymentProcessorService.
 * This service is responsible for routing payment requests to the appropriate payment gateway.
 */
@Service
public class PaymentProcessorServiceImpl implements PaymentProcessorService {
    
    private static final Logger logger = LoggerFactory.getLogger(PaymentProcessorServiceImpl.class);
    
    private final Map<String, PaymentGateway> paymentGateways;
    
    @Autowired
    public PaymentProcessorServiceImpl(List<PaymentGateway> gateways) {
        // Create a map of payment gateways by their name
        this.paymentGateways = gateways.stream()
                .collect(Collectors.toMap(PaymentGateway::getGatewayName, gateway -> gateway));
        
        logger.info("Initialized payment processor with {} gateways: {}", 
                paymentGateways.size(), 
                paymentGateways.keySet());
    }
    
    @Override
    public PaymentResponse processPayment(PaymentRequest paymentRequest) {
        String paymentMethod = paymentRequest.getPaymentMethod();
        String paymentGateway = paymentRequest.getPaymentGateway();
        
        // If payment gateway is not specified, find one that supports the payment method
        if (paymentGateway == null || paymentGateway.isEmpty()) {
            paymentGateway = findGatewayForPaymentMethod(paymentMethod);
            paymentRequest.setPaymentGateway(paymentGateway);
        }
        
        // Get the payment gateway
        PaymentGateway gateway = getPaymentGateway(paymentGateway);
        
        // Process the payment
        logger.info("Processing payment for order {} using gateway {}", 
                paymentRequest.getOrderId(), paymentGateway);
        
        return gateway.processPayment(paymentRequest);
    }
    
    @Override
    public RefundResponse processRefund(RefundRequest refundRequest) {
        String paymentGateway = refundRequest.getPaymentGateway();
        
        // Get the payment gateway
        PaymentGateway gateway = getPaymentGateway(paymentGateway);
        
        // Process the refund
        logger.info("Processing refund for payment {} using gateway {}", 
                refundRequest.getPaymentId(), paymentGateway);
        
        return gateway.processRefund(refundRequest);
    }
    
    @Override
    public PaymentGateway getPaymentGateway(String paymentGateway) {
        PaymentGateway gateway = paymentGateways.get(paymentGateway);
        if (gateway == null) {
            throw new IllegalArgumentException("Payment gateway not found: " + paymentGateway);
        }
        return gateway;
    }
    
    @Override
    public List<String> getSupportedPaymentMethods() {
        return paymentGateways.values().stream()
                .flatMap(gateway -> gateway.getSupportedPaymentMethods().stream())
                .distinct()
                .collect(Collectors.toList());
    }
    
    @Override
    public boolean isPaymentMethodSupported(String paymentMethod) {
        return paymentGateways.values().stream()
                .anyMatch(gateway -> gateway.supportsPaymentMethod(paymentMethod));
    }
    
    /**
     * Find a payment gateway that supports the given payment method
     * @param paymentMethod the payment method
     * @return the name of the payment gateway
     */
    private String findGatewayForPaymentMethod(String paymentMethod) {
        return paymentGateways.values().stream()
                .filter(gateway -> gateway.supportsPaymentMethod(paymentMethod))
                .map(PaymentGateway::getGatewayName)
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("No payment gateway found for payment method: " + paymentMethod));
    }
} 