package com.handmade.ecommerce.payment.api.service;

import com.handmade.ecommerce.payment.api.dto.PaymentDTO;
import java.util.List;

public interface PaymentApiService {
    PaymentDTO createPayment(String orderId, String userId, double amount, String paymentMethod);
    PaymentDTO getPaymentById(String id);
    PaymentDTO getPaymentByOrderId(String orderId);
    List<PaymentDTO> getPaymentsByUserId(String userId);
    List<PaymentDTO> getAllPayments();
    PaymentDTO processPayment(String paymentId);
    PaymentDTO refundPayment(String paymentId, String reason);
    PaymentDTO updatePaymentStatus(String paymentId, String status);
    void deletePayment(String paymentId);
    double calculateTotalAmount(String orderId);
} 