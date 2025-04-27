package com.handmade.ecommerce.return.controller;

import com.handmade.ecommerce.return.model.Return;
import com.handmade.ecommerce.return.model.ReturnStatus;
import com.handmade.ecommerce.return.service.ReturnService;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/returns")
@RequiredArgsConstructor
public class ReturnController {

    private final ReturnService returnService;

    @PostMapping
    public ResponseEntity<Return> createReturn(@RequestBody Return returnRequest) {
        Return createdReturn = returnService.createReturn(returnRequest);
        return new ResponseEntity<>(createdReturn, HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Return> getReturnById(@PathVariable String id) {
        return returnService.getReturnById(id)
                .map(returnEntity -> new ResponseEntity<>(returnEntity, HttpStatus.OK))
                .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @GetMapping("/customer/{customerId}")
    public ResponseEntity<List<Return>> getReturnsByCustomerId(@PathVariable String customerId) {
        List<Return> returns = returnService.getReturnsByCustomerId(customerId);
        return new ResponseEntity<>(returns, HttpStatus.OK);
    }

    @GetMapping("/order/{orderId}")
    public ResponseEntity<List<Return>> getReturnsByOrderId(@PathVariable String orderId) {
        List<Return> returns = returnService.getReturnsByOrderId(orderId);
        return new ResponseEntity<>(returns, HttpStatus.OK);
    }

    @GetMapping("/status/{status}")
    public ResponseEntity<List<Return>> getReturnsByStatus(@PathVariable ReturnStatus status) {
        List<Return> returns = returnService.getReturnsByStatus(status);
        return new ResponseEntity<>(returns, HttpStatus.OK);
    }

    @PatchMapping("/{id}/status")
    public ResponseEntity<Return> updateReturnStatus(
            @PathVariable String id,
            @RequestParam ReturnStatus status) {
        try {
            Return updatedReturn = returnService.updateReturnStatus(id, status);
            return new ResponseEntity<>(updatedReturn, HttpStatus.OK);
        } catch (EntityNotFoundException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @PatchMapping("/{id}/tracking")
    public ResponseEntity<Return> updateReturnTrackingNumber(
            @PathVariable String id,
            @RequestParam String trackingNumber) {
        try {
            Return updatedReturn = returnService.updateReturnTrackingNumber(id, trackingNumber);
            return new ResponseEntity<>(updatedReturn, HttpStatus.OK);
        } catch (EntityNotFoundException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteReturn(@PathVariable String id) {
        returnService.deleteReturn(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @PostMapping("/{id}/refund")
    public ResponseEntity<Return> processRefund(
            @PathVariable String id,
            @RequestParam Double refundAmount) {
        try {
            Return refundedReturn = returnService.processRefund(id, refundAmount);
            return new ResponseEntity<>(refundedReturn, HttpStatus.OK);
        } catch (EntityNotFoundException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
} 