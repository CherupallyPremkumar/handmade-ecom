package com.handmade.ecommerce.payment.api.service.impl;

import com.handmade.ecommerce.payment.api.dto.PaymentDTO;
import com.handmade.ecommerce.payment.api.service.PaymentApiService;
import com.handmade.ecommerce.payment.model.Payment;
import com.handmade.ecommerce.payment.service.PaymentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class PaymentApiServiceImpl implements PaymentApiService {

    @Autowired
    private PaymentService paymentService;

    @Override
    public PaymentDTO createPayment(String orderId, String userId, double amount, String paymentMethod) {
        Payment payment = paymentService.createPayment(orderId, userId, amount, paymentMethod);
        return convertToDTO(payment);
    }

    @Override
    public PaymentDTO getPaymentById(String id) {
        Payment payment = paymentService.getPaymentById(id);
        return convertToDTO(payment);
    }

    @Override
    public PaymentDTO getPaymentByOrderId(String orderId) {
        Payment payment = paymentService.getPaymentByOrderId(orderId);
        return convertToDTO(payment);
    }

    @Override
    public List<PaymentDTO> getPaymentsByUserId(String userId) {
        return paymentService.getPaymentsByUserId(userId).stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    @Override
    public List<PaymentDTO> getAllPayments() {
        return paymentService.getAllPayments().stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    @Override
    public PaymentDTO processPayment(String paymentId) {
        Payment payment = paymentService.processPayment(paymentId);
        return convertToDTO(payment);
    }

    @Override
    public PaymentDTO refundPayment(String paymentId, String reason) {
        Payment payment = paymentService.refundPayment(paymentId, reason);
        return convertToDTO(payment);
    }

    @Override
    public PaymentDTO updatePaymentStatus(String paymentId, String status) {
        Payment payment = paymentService.updatePaymentStatus(paymentId, status);
        return convertToDTO(payment);
    }

    @Override
    public void deletePayment(String paymentId) {
        paymentService.deletePayment(paymentId);
    }

    @Override
    public double calculateTotalAmount(String orderId) {
        return paymentService.calculateTotalAmount(orderId);
    }

    private PaymentDTO convertToDTO(Payment payment) {
        PaymentDTO dto = new PaymentDTO();
        dto.setId(payment.getId());
        dto.setOrderId(payment.getOrderId());
        dto.setUserId(payment.getUserId());
        dto.setAmount(payment.getAmount());
        dto.setCurrency(payment.getCurrency());
        dto.setPaymentMethod(payment.getPaymentMethod());
        dto.setPaymentStatus(payment.getPaymentStatus());
        dto.setTransactionId(payment.getTransactionId());
        dto.setPaymentGateway(payment.getPaymentGateway());
        dto.setPaymentGatewayResponse(payment.getPaymentGatewayResponse());
        dto.setPaymentDate(payment.getPaymentDate().toString());
        dto.setErrorMessage(payment.getErrorMessage());
        dto.setRefundReason(payment.getRefundReason());
        dto.setRefundDate(payment.getRefundDate() != null ? payment.getRefundDate().toString() : null);
        dto.setBillingAddress(payment.getBillingAddress());
        dto.setShippingAddress(payment.getShippingAddress());
        return dto;
    }
} 