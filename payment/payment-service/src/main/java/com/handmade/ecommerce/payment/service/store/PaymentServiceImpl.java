package com.handmade.ecommerce.payment.service.store;

import org.chenile.base.service.ChenileServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.handmade.ecommerce.payment.model.Payment;
import com.handmade.ecommerce.payment.model.PaymentRequest;
import com.handmade.ecommerce.payment.model.PaymentResponse;
import com.handmade.ecommerce.payment.model.RefundRequest;
import com.handmade.ecommerce.payment.model.RefundResponse;
import com.handmade.ecommerce.payment.service.PaymentService;
import com.handmade.ecommerce.payment.service.PaymentProcessorService;
import com.handmade.ecommerce.payment.configuration.dao.PaymentRepository;
import com.handmade.ecommerce.order.service.OrderService;
import java.util.List;
import java.time.LocalDateTime;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

@Service
public class PaymentServiceImpl extends ChenileServiceImpl<Payment> implements PaymentService {
    
    @Autowired
    private PaymentRepository paymentRepository;

    @Autowired
    private OrderService orderService;
    
    @Autowired
    private PaymentProcessorService paymentProcessorService;

    @Override
    @Transactional
    public Payment createPayment(String orderId, String userId, double amount, String paymentMethod) {
        // Create a payment request
        PaymentRequest paymentRequest = new PaymentRequest();
        paymentRequest.setOrderId(orderId);
        paymentRequest.setUserId(userId);
        paymentRequest.setAmount(BigDecimal.valueOf(amount));
        paymentRequest.setPaymentMethod(paymentMethod);
        paymentRequest.setCurrency("USD"); // Default currency
        
        // Create a payment entity
        Payment payment = new Payment();
        payment.setOrderId(orderId);
        payment.setUserId(userId);
        payment.setAmount(BigDecimal.valueOf(amount));
        payment.setPaymentMethod(paymentMethod);
        payment.setPaymentStatus("PENDING");
        payment.setCurrency("USD"); // Default currency
        
        // Save the payment entity
        payment = store(payment);
        
        // Set the payment ID in the request
        paymentRequest.setPaymentId(payment.getId());
        
        return payment;
    }

    @Override
    public Payment getPaymentById(String id) {
        return retrieve(id);
    }

    @Override
    public Payment getPaymentByOrderId(String orderId) {
        return paymentRepository.findByOrderId(orderId)
                .orElseThrow(() -> new RuntimeException("Payment not found for order: " + orderId));
    }

    @Override
    public List<Payment> getPaymentsByUserId(String userId) {
        return paymentRepository.findByUserId(userId);
    }

    @Override
    public List<Payment> getAllPayments() {
        return paymentRepository.findAll();
    }

    @Override
    @Transactional
    public Payment processPayment(String paymentId) {
        Payment payment = retrieve(paymentId);
        
        try {
            // Create a payment request
            PaymentRequest paymentRequest = new PaymentRequest();
            paymentRequest.setPaymentId(paymentId);
            paymentRequest.setOrderId(payment.getOrderId());
            paymentRequest.setUserId(payment.getUserId());
            paymentRequest.setAmount(payment.getAmount());
            paymentRequest.setCurrency(payment.getCurrency());
            paymentRequest.setPaymentMethod(payment.getPaymentMethod());
            paymentRequest.setPaymentGateway(payment.getPaymentGateway());
            paymentRequest.setDescription("Payment for order " + payment.getOrderId());
            
            // Process the payment
            PaymentResponse response = paymentProcessorService.processPayment(paymentRequest);
            
            // Update the payment entity
            payment.setPaymentStatus(response.getStatus());
            payment.setTransactionId(response.getTransactionId());
            payment.setPaymentGateway(response.getPaymentGateway());
            payment.setPaymentGatewayResponse(response.getGatewayResponse().toString());
            payment.setErrorMessage(response.getErrorMessage());
            payment.setErrorCode(response.getErrorCode());
            
            // Update order payment status
            if ("SUCCESS".equals(response.getStatus())) {
                orderService.updatePaymentStatus(payment.getOrderId(), "PAID");
            } else if ("FAILED".equals(response.getStatus())) {
                orderService.updatePaymentStatus(payment.getOrderId(), "PAYMENT_FAILED");
            }
            
            return store(payment);
        } catch (Exception e) {
            payment.setPaymentStatus("FAILED");
            payment.setErrorMessage(e.getMessage());
            return store(payment);
        }
    }

    @Override
    @Transactional
    public Payment refundPayment(String paymentId, String reason) {
        Payment payment = retrieve(paymentId);
        
        if (!"SUCCESS".equals(payment.getPaymentStatus())) {
            throw new RuntimeException("Cannot refund payment that is not successful");
        }
        
        try {
            // Create a refund request
            RefundRequest refundRequest = new RefundRequest();
            refundRequest.setPaymentId(paymentId);
            refundRequest.setOrderId(payment.getOrderId());
            refundRequest.setTransactionId(payment.getTransactionId());
            refundRequest.setAmount(payment.getAmount());
            refundRequest.setCurrency(payment.getCurrency());
            refundRequest.setReason(reason);
            refundRequest.setPaymentGateway(payment.getPaymentGateway());
            
            // Process the refund
            RefundResponse response = paymentProcessorService.processRefund(refundRequest);
            
            // Update the payment entity
            payment.setPaymentStatus("REFUNDED");
            payment.setRefundReason(reason);
            payment.setRefundDate(LocalDateTime.now());
            payment.setRefundId(response.getRefundId());
            
            // Update order payment status
            orderService.updatePaymentStatus(payment.getOrderId(), "REFUNDED");
            
            return store(payment);
        } catch (Exception e) {
            payment.setErrorMessage("Refund failed: " + e.getMessage());
            return store(payment);
        }
    }

    @Override
    @Transactional
    public Payment updatePaymentStatus(String paymentId, String status) {
        Payment payment = retrieve(paymentId);
        payment.setPaymentStatus(status);
        return store(payment);
    }

    @Override
    @Transactional
    public void deletePayment(String paymentId) {
        paymentRepository.deleteById(paymentId);
    }

    @Override
    public double calculateTotalAmount(String orderId) {
        return orderService.calculateOrderTotal(orderId);
    }
} 