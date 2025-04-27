package com.handmade.ecommerce.shipping.repository;

import com.handmade.ecommerce.shipping.model.Shipment;
import com.handmade.ecommerce.shipping.model.ShipmentStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public interface ShipmentRepository extends JpaRepository<Shipment, String> {
    List<Shipment> findByCustomerId(String customerId);
    List<Shipment> findByOrderId(String orderId);
    List<Shipment> findByStatus(ShipmentStatus status);
    Optional<Shipment> findByTrackingNumber(String trackingNumber);
    List<Shipment> findByCarrier(String carrier);
} 