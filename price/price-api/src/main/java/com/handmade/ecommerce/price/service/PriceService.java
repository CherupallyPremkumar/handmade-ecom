package com.handmade.ecommerce.price.service;

import com.handmade.ecommerce.price.model.Price;


import java.util.List;
import java.util.Optional;

/**
 * Service interface for managing product prices.
 */
public interface PriceService {
    
    /**
     * Get all prices for a product
     * @param productId the ID of the product
     * @return a list of prices for the product
     */
    List<Price> getPricesByProductId(String productId);
    
    /**
     * Get the current active price for a product
     * @param productId the ID of the product
     * @return the current active price for the product
     */
    Optional<Price> getCurrentPrice(String productId);
    
    /**
     * Create a new price for a product
     * @param price the price to create
     * @return the created price
     */
    Price createPrice(Price price);
    
    /**
     * Update an existing price
     * @param id the ID of the price to update
     * @param price the updated price data
     * @return the updated price
     */
    Optional<Price> updatePrice(String id, Price price);
    
    /**
     * Activate a price and deactivate all other prices for the same product
     * @param id the ID of the price to activate
     * @return the activated price
     */
    Optional<Price> activatePrice(String id);
    
    /**
     * Delete a price
     * @param id the ID of the price to delete
     */
    void deletePrice(String id);
}

