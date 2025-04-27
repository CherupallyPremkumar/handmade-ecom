package com.handmade.ecommerce.shipping.service;

import com.handmade.ecommerce.shipping.model.Shipment;
import com.handmade.ecommerce.shipping.model.ShipmentStatus;
import com.handmade.ecommerce.shipping.provider.ShippingRate;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

/**
 * Service interface for managing shipments
 */
public interface ShipmentService {

    /**
     * Create a new shipment
     *
     * @param shipment the shipment to create
     * @return the created shipment
     */
    Shipment createShipment(Shipment shipment);

    /**
     * Get a shipment by its ID
     *
     * @param id the shipment ID
     * @return the shipment
     */
    Optional<Shipment> getShipmentById(String id);

    /**
     * Get a shipment by its tracking number
     *
     * @param trackingNumber the tracking number
     * @return the shipment
     */
    Optional<Shipment> getShipmentByTrackingNumber(String trackingNumber);

    /**
     * Get all shipments for a customer
     *
     * @param customerId the customer ID
     * @return list of shipments
     */
    List<Shipment> getShipmentsByCustomerId(String customerId);

    /**
     * Get all shipments for an order
     *
     * @param orderId the order ID
     * @return list of shipments
     */
    List<Shipment> getShipmentsByOrderId(String orderId);

    /**
     * Get all shipments with a specific status
     *
     * @param status the shipment status
     * @return list of shipments
     */
    List<Shipment> getShipmentsByStatus(ShipmentStatus status);

    /**
     * Get all shipments for a specific carrier
     *
     * @param carrier the carrier name
     * @return list of shipments
     */
    List<Shipment> getShipmentsByCarrier(String carrier);

    /**
     * Update the status of a shipment
     *
     * @param id the shipment ID
     * @param status the new status
     * @return the updated shipment
     */
    Shipment updateShipmentStatus(String id, ShipmentStatus status);

    /**
     * Update the tracking number of a shipment
     *
     * @param id the shipment ID
     * @param trackingNumber the new tracking number
     * @return the updated shipment
     */
    Shipment updateTrackingNumber(String id, String trackingNumber);

    /**
     * Update the carrier of a shipment
     *
     * @param id the shipment ID
     * @param carrier the new carrier
     * @return the updated shipment
     */
    Shipment updateCarrier(String id, String carrier);

    /**
     * Update the estimated delivery date of a shipment
     *
     * @param id the shipment ID
     * @param estimatedDeliveryDate the new estimated delivery date
     * @return the updated shipment
     */
    Shipment updateEstimatedDeliveryDate(String id, LocalDateTime estimatedDeliveryDate);

    /**
     * Mark a shipment as delivered
     *
     * @param id the shipment ID
     * @param deliveryDate the delivery date
     * @return the updated shipment
     */
    Shipment markAsDelivered(String id, LocalDateTime deliveryDate);

    /**
     * Delete a shipment
     *
     * @param id the shipment ID
     */
    void deleteShipment(String id);

    /**
     * Calculate shipping rates for a shipment
     *
     * @param shipment the shipment to calculate rates for
     * @return list of available shipping rates
     */
    List<ShippingRate> calculateShippingRates(Shipment shipment);

    /**
     * Schedule a pickup for a shipment
     *
     * @param id the shipment ID
     * @param pickupDate the pickup date
     * @return the updated shipment
     */
    Shipment schedulePickup(String id, LocalDateTime pickupDate);

    /**
     * Cancel a shipment
     *
     * @param id the shipment ID
     * @return the updated shipment
     */
    Shipment cancelShipment(String id);
} 