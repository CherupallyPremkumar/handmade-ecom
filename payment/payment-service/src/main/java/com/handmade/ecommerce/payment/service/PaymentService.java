package com.handmade.ecommerce.payment.service;

import org.chenile.base.service.ChenileService;
import com.handmade.ecommerce.payment.model.Payment;
import java.util.List;

@ChenileService
public interface PaymentService {
    Payment createPayment(String orderId, String userId, double amount, String paymentMethod);
    Payment getPaymentById(String id);
    Payment getPaymentByOrderId(String orderId);
    List<Payment> getPaymentsByUserId(String userId);
    List<Payment> getAllPayments();
    Payment processPayment(String paymentId);
    Payment refundPayment(String paymentId, String reason);
    Payment updatePaymentStatus(String paymentId, String status);
    void deletePayment(String paymentId);
    double calculateTotalAmount(String orderId);
} 