package com.handmade.ecommerce.price.controller;

import com.handmade.ecommerce.price.model.Price;
import com.handmade.ecommerce.price.service.PriceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/prices")
public class PriceController {

    private final PriceService priceService;

    @Autowired
    public PriceController(PriceService priceService) {
        this.priceService = priceService;
    }

    /**
     * Get all prices for a product
     * @param productId the ID of the product
     * @return list of prices for the product
     */
    @GetMapping("/product/{productId}")
    public ResponseEntity<List<Price>> getPricesByProductId(@PathVariable String productId) {
        return ResponseEntity.ok(priceService.getPricesByProductId(productId));
    }

    /**
     * Get the current active price for a product
     * @param productId the ID of the product
     * @return the current active price
     */
    @GetMapping("/product/{productId}/current")
    public ResponseEntity<Price> getCurrentPrice(@PathVariable String productId) {
        return priceService.getCurrentPrice(productId)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    /**
     * Create a new price
     * @param price the price to create
     * @return the created price
     */
    @PostMapping
    public ResponseEntity<Price> createPrice(@RequestBody Price price) {
        return new ResponseEntity<>(priceService.createPrice(price), HttpStatus.CREATED);
    }

    /**
     * Update an existing price
     * @param id the ID of the price to update
     * @param price the updated price data
     * @return the updated price
     */
    @PutMapping("/{id}")
    public ResponseEntity<Price> updatePrice(@PathVariable String id, @RequestBody Price price) {
        return priceService.updatePrice(id, price)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    /**
     * Activate a price
     * @param id the ID of the price to activate
     * @return the activated price
     */
    @PutMapping("/{id}/activate")
    public ResponseEntity<Price> activatePrice(@PathVariable String id) {
        return priceService.activatePrice(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    /**
     * Delete a price
     * @param id the ID of the price to delete
     * @return no content response
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePrice(@PathVariable String id) {
        priceService.deletePrice(id);
        return ResponseEntity.noContent().build();
    }
} 