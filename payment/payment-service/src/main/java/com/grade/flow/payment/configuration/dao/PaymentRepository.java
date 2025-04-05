package com.handmade.ecommerce.payment.configuration.dao;

import com.handmade.ecommerce.payment.model.Payment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository  public interface PaymentRepository extends JpaRepository<Payment,String> {}
