package com.handmade.ecommerce.shipping.provider;

import com.handmade.ecommerce.shipping.model.Shipment;
import java.time.LocalDateTime;
import java.util.List;

/**
 * Interface defining the contract for shipping providers
 */
public interface ShippingProvider {
    
    /**
     * Get the name of the shipping provider
     * @return the provider name
     */
    String getProviderName();
    
    /**
     * Create a new shipment
     * @param shipment the shipment to create
     * @return the created shipment with tracking number and other provider-specific details
     */
    Shipment createShipment(Shipment shipment);
    
    /**
     * Update the status of a shipment
     * @param trackingNumber the tracking number
     * @return the updated shipment
     */
    Shipment updateShipmentStatus(String trackingNumber);
    
    /**
     * Calculate shipping rates for a shipment
     * @param shipment the shipment to calculate rates for
     * @return list of available shipping rates
     */
    List<ShippingRate> calculateRates(Shipment shipment);
    
    /**
     * Schedule a pickup for a shipment
     * @param shipment the shipment to schedule pickup for
     * @param pickupDate the pickup date
     * @return the updated shipment
     */
    Shipment schedulePickup(Shipment shipment, LocalDateTime pickupDate);
    
    /**
     * Cancel a shipment
     * @param trackingNumber the tracking number
     * @return the canceled shipment
     */
    Shipment cancelShipment(String trackingNumber);
} 