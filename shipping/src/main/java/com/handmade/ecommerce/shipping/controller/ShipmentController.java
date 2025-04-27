package com.handmade.ecommerce.shipping.controller;

import com.handmade.ecommerce.shipping.model.Shipment;
import com.handmade.ecommerce.shipping.model.ShipmentStatus;
import com.handmade.ecommerce.shipping.service.ShipmentService;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/v1/shipments")
@RequiredArgsConstructor
public class ShipmentController {

    private final ShipmentService shipmentService;

    @PostMapping
    public ResponseEntity<Shipment> createShipment(@RequestBody Shipment shipment) {
        Shipment createdShipment = shipmentService.createShipment(shipment);
        return new ResponseEntity<>(createdShipment, HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Shipment> getShipmentById(@PathVariable String id) {
        return shipmentService.getShipmentById(id)
                .map(shipment -> new ResponseEntity<>(shipment, HttpStatus.OK))
                .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @GetMapping("/tracking/{trackingNumber}")
    public ResponseEntity<Shipment> getShipmentByTrackingNumber(@PathVariable String trackingNumber) {
        return shipmentService.getShipmentByTrackingNumber(trackingNumber)
                .map(shipment -> new ResponseEntity<>(shipment, HttpStatus.OK))
                .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @GetMapping("/customer/{customerId}")
    public ResponseEntity<List<Shipment>> getShipmentsByCustomerId(@PathVariable String customerId) {
        List<Shipment> shipments = shipmentService.getShipmentsByCustomerId(customerId);
        return new ResponseEntity<>(shipments, HttpStatus.OK);
    }

    @GetMapping("/order/{orderId}")
    public ResponseEntity<List<Shipment>> getShipmentsByOrderId(@PathVariable String orderId) {
        List<Shipment> shipments = shipmentService.getShipmentsByOrderId(orderId);
        return new ResponseEntity<>(shipments, HttpStatus.OK);
    }

    @GetMapping("/status/{status}")
    public ResponseEntity<List<Shipment>> getShipmentsByStatus(@PathVariable ShipmentStatus status) {
        List<Shipment> shipments = shipmentService.getShipmentsByStatus(status);
        return new ResponseEntity<>(shipments, HttpStatus.OK);
    }

    @GetMapping("/carrier/{carrier}")
    public ResponseEntity<List<Shipment>> getShipmentsByCarrier(@PathVariable String carrier) {
        List<Shipment> shipments = shipmentService.getShipmentsByCarrier(carrier);
        return new ResponseEntity<>(shipments, HttpStatus.OK);
    }

    @PatchMapping("/{id}/status")
    public ResponseEntity<Shipment> updateShipmentStatus(
            @PathVariable String id,
            @RequestParam ShipmentStatus status) {
        try {
            Shipment updatedShipment = shipmentService.updateShipmentStatus(id, status);
            return new ResponseEntity<>(updatedShipment, HttpStatus.OK);
        } catch (EntityNotFoundException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @PatchMapping("/{id}/tracking")
    public ResponseEntity<Shipment> updateTrackingNumber(
            @PathVariable String id,
            @RequestParam String trackingNumber) {
        try {
            Shipment updatedShipment = shipmentService.updateTrackingNumber(id, trackingNumber);
            return new ResponseEntity<>(updatedShipment, HttpStatus.OK);
        } catch (EntityNotFoundException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @PatchMapping("/{id}/carrier")
    public ResponseEntity<Shipment> updateCarrier(
            @PathVariable String id,
            @RequestParam String carrier) {
        try {
            Shipment updatedShipment = shipmentService.updateCarrier(id, carrier);
            return new ResponseEntity<>(updatedShipment, HttpStatus.OK);
        } catch (EntityNotFoundException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @PatchMapping("/{id}/estimated-delivery")
    public ResponseEntity<Shipment> updateEstimatedDeliveryDate(
            @PathVariable String id,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime estimatedDeliveryDate) {
        try {
            Shipment updatedShipment = shipmentService.updateEstimatedDeliveryDate(id, estimatedDeliveryDate);
            return new ResponseEntity<>(updatedShipment, HttpStatus.OK);
        } catch (EntityNotFoundException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @PostMapping("/{id}/deliver")
    public ResponseEntity<Shipment> markAsDelivered(
            @PathVariable String id,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime deliveryDate) {
        try {
            Shipment deliveredShipment = shipmentService.markAsDelivered(id, 
                    deliveryDate != null ? deliveryDate : LocalDateTime.now());
            return new ResponseEntity<>(deliveredShipment, HttpStatus.OK);
        } catch (EntityNotFoundException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteShipment(@PathVariable String id) {
        shipmentService.deleteShipment(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
} 