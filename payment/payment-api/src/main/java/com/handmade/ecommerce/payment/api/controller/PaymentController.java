package com.handmade.ecommerce.payment.api.controller;

import com.handmade.ecommerce.payment.api.dto.PaymentDTO;
import com.handmade.ecommerce.payment.api.service.PaymentApiService;
import org.chenile.base.controller.ChenileController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/payments")
@ChenileController
public class PaymentController {

    @Autowired
    private PaymentApiService paymentApiService;

    @PostMapping
    public ResponseEntity<PaymentDTO> createPayment(
            @RequestParam String orderId,
            @RequestParam String userId,
            @RequestParam double amount,
            @RequestParam String paymentMethod) {
        return ResponseEntity.ok(paymentApiService.createPayment(orderId, userId, amount, paymentMethod));
    }

    @GetMapping("/{id}")
    public ResponseEntity<PaymentDTO> getPayment(@PathVariable String id) {
        return ResponseEntity.ok(paymentApiService.getPaymentById(id));
    }

    @GetMapping("/order/{orderId}")
    public ResponseEntity<PaymentDTO> getPaymentByOrderId(@PathVariable String orderId) {
        return ResponseEntity.ok(paymentApiService.getPaymentByOrderId(orderId));
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<List<PaymentDTO>> getPaymentsByUserId(@PathVariable String userId) {
        return ResponseEntity.ok(paymentApiService.getPaymentsByUserId(userId));
    }

    @GetMapping
    public ResponseEntity<List<PaymentDTO>> getAllPayments() {
        return ResponseEntity.ok(paymentApiService.getAllPayments());
    }

    @PostMapping("/{paymentId}/process")
    public ResponseEntity<PaymentDTO> processPayment(@PathVariable String paymentId) {
        return ResponseEntity.ok(paymentApiService.processPayment(paymentId));
    }

    @PostMapping("/{paymentId}/refund")
    public ResponseEntity<PaymentDTO> refundPayment(
            @PathVariable String paymentId,
            @RequestParam String reason) {
        return ResponseEntity.ok(paymentApiService.refundPayment(paymentId, reason));
    }

    @PutMapping("/{paymentId}/status")
    public ResponseEntity<PaymentDTO> updatePaymentStatus(
            @PathVariable String paymentId,
            @RequestParam String status) {
        return ResponseEntity.ok(paymentApiService.updatePaymentStatus(paymentId, status));
    }

    @DeleteMapping("/{paymentId}")
    public ResponseEntity<Void> deletePayment(@PathVariable String paymentId) {
        paymentApiService.deletePayment(paymentId);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/order/{orderId}/total")
    public ResponseEntity<Double> calculateTotalAmount(@PathVariable String orderId) {
        return ResponseEntity.ok(paymentApiService.calculateTotalAmount(orderId));
    }
} 