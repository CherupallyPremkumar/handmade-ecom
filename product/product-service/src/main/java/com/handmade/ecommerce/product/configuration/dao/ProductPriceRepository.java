package com.handmade.ecommerce.product.configuration.dao;

import com.handmade.ecommerce.product.model.ProductPrice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductPriceRepository extends JpaRepository<ProductPrice, String> {
    
    /**
     * Find all prices for a specific product
     * 
     * @param productId The product ID
     * @return List of prices for the product
     */
    List<ProductPrice> findByProductId(String productId);
    
    /**
     * Find product prices by product ID and location code
     * 
     * @param productId The product ID
     * @param locationCode The location code (e.g., "US", "IN")
     * @return List of matching prices
     */
    List<ProductPrice> findByProductIdAndLocationCode(String productId, String locationCode);
    
    /**
     * Find product prices by product ID, location code and currency code
     * 
     * @param productId The product ID
     * @param locationCode The location code
     * @param currencyCode The currency code (e.g., "USD", "INR")
     * @return The matching product price (usually single result)
     */
    ProductPrice findByProductIdAndLocationCodeAndCurrencyCode(String productId, String locationCode, String currencyCode);
} 