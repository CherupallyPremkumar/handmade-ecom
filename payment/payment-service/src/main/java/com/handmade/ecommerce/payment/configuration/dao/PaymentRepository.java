package com.handmade.ecommerce.payment.configuration.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.handmade.ecommerce.payment.model.Payment;
import java.util.List;
import java.util.Optional;

@Repository
public interface PaymentRepository extends JpaRepository<Payment, String> {
    Optional<Payment> findByOrderId(String orderId);
    List<Payment> findByUserId(String userId);
    List<Payment> findByPaymentStatus(String paymentStatus);
    List<Payment> findByPaymentMethod(String paymentMethod);
} 