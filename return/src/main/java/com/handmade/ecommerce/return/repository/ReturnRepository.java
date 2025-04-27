package com.handmade.ecommerce.return.repository;

import com.handmade.ecommerce.return.model.Return;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface ReturnRepository extends JpaRepository<Return, String> {
    List<Return> findByCustomerId(String customerId);
    List<Return> findByOrderId(String orderId);
    List<Return> findByStatus(String status);
} 