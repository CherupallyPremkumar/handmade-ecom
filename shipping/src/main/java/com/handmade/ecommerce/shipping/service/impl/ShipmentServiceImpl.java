package com.handmade.ecommerce.shipping.service.impl;

import com.handmade.ecommerce.shipping.event.ShipmentCreatedEvent;
import com.handmade.ecommerce.shipping.event.ShipmentEventPublisher;
import com.handmade.ecommerce.shipping.event.ShipmentStatusUpdatedEvent;
import com.handmade.ecommerce.shipping.model.Shipment;
import com.handmade.ecommerce.shipping.model.ShipmentStatus;
import com.handmade.ecommerce.shipping.provider.ShippingProvider;
import com.handmade.ecommerce.shipping.provider.ShippingProviderFactory;
import com.handmade.ecommerce.shipping.provider.ShippingRate;
import com.handmade.ecommerce.shipping.repository.ShipmentRepository;
import com.handmade.ecommerce.shipping.service.ShipmentService;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

/**
 * Implementation of ShipmentService
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class ShipmentServiceImpl implements ShipmentService {

    private final ShipmentRepository shipmentRepository;
    private final ShippingProviderFactory shippingProviderFactory;
    private final ShipmentEventPublisher eventPublisher;

    @Override
    @Transactional
    public Shipment createShipment(Shipment shipment) {
        log.info("Creating shipment for order: {}", shipment.getOrderId());
        
        // Get the shipping provider
        ShippingProvider provider = shippingProviderFactory.getProvider(shipment.getCarrier());
        
        // Create the shipment using the provider
        Shipment createdShipment = provider.createShipment(shipment);
        
        // Save the shipment to the database
        Shipment savedShipment = shipmentRepository.save(createdShipment);
        
        // Publish shipment created event
        eventPublisher.publishShipmentCreated(ShipmentCreatedEvent.builder()
                .shipmentId(savedShipment.getId())
                .orderId(savedShipment.getOrderId())
                .customerId(savedShipment.getCustomerId())
                .carrier(savedShipment.getCarrier())
                .trackingNumber(savedShipment.getTrackingNumber())
                .serviceLevel(savedShipment.getServiceLevel())
                .shippingCost(savedShipment.getShippingCost())
                .currency(savedShipment.getCurrency())
                .fromAddress(savedShipment.getFromAddress())
                .toAddress(savedShipment.getToAddress())
                .build());
        
        return savedShipment;
    }

    @Override
    public Shipment getShipmentById(String id) {
        return shipmentRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Shipment not found with id: " + id));
    }

    @Override
    public Shipment getShipmentByTrackingNumber(String trackingNumber) {
        return shipmentRepository.findByTrackingNumber(trackingNumber)
                .orElseThrow(() -> new EntityNotFoundException("Shipment not found with tracking number: " + trackingNumber));
    }

    @Override
    public List<Shipment> getShipmentsByCustomerId(String customerId) {
        return shipmentRepository.findByCustomerId(customerId);
    }

    @Override
    public List<Shipment> getShipmentsByOrderId(String orderId) {
        return shipmentRepository.findByOrderId(orderId);
    }

    @Override
    public List<Shipment> getShipmentsByStatus(ShipmentStatus status) {
        return shipmentRepository.findByStatus(status);
    }

    @Override
    public List<Shipment> getShipmentsByCarrier(String carrier) {
        return shipmentRepository.findByCarrier(carrier);
    }

    @Override
    @Transactional
    public Shipment updateShipmentStatus(String id, ShipmentStatus status) {
        Shipment shipment = getShipmentById(id)
                .orElseThrow(() -> new EntityNotFoundException("Shipment not found with id: " + id));
        
        ShipmentStatus oldStatus = shipment.getStatus();
        
        // Get the shipping provider
        ShippingProvider provider = shippingProviderFactory.getProvider(shipment.getCarrier());
        
        // Update the status using the provider
        Shipment updatedShipment = provider.updateShipmentStatus(shipment.getTrackingNumber());
        
        // Update the status in our database
        shipment.setStatus(updatedShipment.getStatus());
        Shipment savedShipment = shipmentRepository.save(shipment);
        
        // Publish shipment status updated event
        eventPublisher.publishShipmentStatusUpdated(ShipmentStatusUpdatedEvent.builder()
                .shipmentId(savedShipment.getId())
                .orderId(savedShipment.getOrderId())
                .carrier(savedShipment.getCarrier())
                .trackingNumber(savedShipment.getTrackingNumber())
                .oldStatus(oldStatus)
                .newStatus(savedShipment.getStatus())
                .statusDetails(updatedShipment.getStatusDetails())
                .location(updatedShipment.getCurrentLocation())
                .build());
        
        return savedShipment;
    }

    @Override
    @Transactional
    public Shipment updateTrackingNumber(String id, String trackingNumber) {
        Shipment shipment = getShipmentById(id);
        shipment.setTrackingNumber(trackingNumber);
        return shipmentRepository.save(shipment);
    }

    @Override
    @Transactional
    public Shipment updateCarrier(String id, String carrier) {
        Shipment shipment = getShipmentById(id);
        shipment.setCarrier(carrier);
        return shipmentRepository.save(shipment);
    }

    @Override
    @Transactional
    public Shipment updateEstimatedDeliveryDate(String id, LocalDateTime estimatedDeliveryDate) {
        Shipment shipment = getShipmentById(id);
        shipment.setEstimatedDeliveryDate(estimatedDeliveryDate);
        return shipmentRepository.save(shipment);
    }

    @Override
    @Transactional
    public Shipment markAsDelivered(String id, LocalDateTime deliveryDate) {
        Shipment shipment = getShipmentById(id);
        shipment.setStatus(ShipmentStatus.DELIVERED);
        shipment.setDeliveredDate(deliveryDate != null ? deliveryDate : LocalDateTime.now());
        return shipmentRepository.save(shipment);
    }

    @Override
    @Transactional
    public void deleteShipment(String id) {
        Shipment shipment = getShipmentById(id);
        shipmentRepository.delete(shipment);
    }

    @Override
    public List<ShippingRate> calculateShippingRates(Shipment shipment) {
        // Get the shipping provider
        ShippingProvider provider = shippingProviderFactory.getProvider(shipment.getCarrier());
        
        // Calculate rates using the provider
        return provider.calculateRates(shipment);
    }

    @Override
    @Transactional
    public Shipment schedulePickup(String id, LocalDateTime pickupDate) {
        Shipment shipment = getShipmentById(id);
        
        // Get the shipping provider
        ShippingProvider provider = shippingProviderFactory.getProvider(shipment.getCarrier());
        
        // Schedule pickup using the provider
        Shipment updatedShipment = provider.schedulePickup(shipment, pickupDate);
        
        // Update the shipment in our database
        shipment.setStatus(updatedShipment.getStatus());
        return shipmentRepository.save(shipment);
    }

    @Override
    @Transactional
    public Shipment cancelShipment(String id) {
        Shipment shipment = getShipmentById(id);
        
        // Get the shipping provider
        ShippingProvider provider = shippingProviderFactory.getProvider(shipment.getCarrier());
        
        // Cancel the shipment using the provider
        Shipment canceledShipment = provider.cancelShipment(shipment.getTrackingNumber());
        
        // Update the shipment in our database
        shipment.setStatus(canceledShipment.getStatus());
        return shipmentRepository.save(shipment);
    }
} 