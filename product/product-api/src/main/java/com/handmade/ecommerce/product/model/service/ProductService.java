package com.handmade.ecommerce.product.model.service;

import com.handmade.ecommerce.price.dto.BaseDTO;
import com.handmade.ecommerce.price.dto.CreatePriceRequestDTO;
import com.handmade.ecommerce.product.model.Product;
import com.handmade.ecommerce.product.model.ProductPrice;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

/**
 * Service interface for product management operations
 */
public interface ProductService {

    /**
     * Create a new product
     * 
     * @param product The product to create
     * @return The created product
     */
    Product createProduct(Product product);
    
    /**
     * Update an existing product
     * 
     * @param product The product with updated information
     * @return The updated product
     */
    Product updateProduct(Product product);
    
    /**
     * Find a product by its ID
     * 
     * @param id The product ID
     * @return Optional containing the product if found
     */
    Optional<Product> findProductById(String id);


    
    /**
     * Find products by material
     * 
     * @param material The material name
     * @return List of products with the specified material
     */
    List<Product> findProductsByMaterial(String material);
    
    /**
     * Add a new price for a product
     * 
     * @param productId The product ID
     * @param locationCode Location code (e.g., "US", "IN")
     * @param currencyCode Currency code (e.g., "USD", "INR")
     * @param price The price value
     * @return The created product price
     */
    ProductPrice addProductPrice(CreatePriceRequestDTO baseDTO);
    
    /**
     * Update an existing product price
     * 
     * @param priceId The price ID
     * @param newPrice The new price value
     * @return The updated price entity
     */
    ProductPrice updateProductPrice(String priceId, BigDecimal newPrice);
    
    /**
     * Get all prices for a product
     * 
     * @param productId The product ID
     * @return List of prices for the product
     */
    List<ProductPrice> getProductPrices(String productId);
    
    /**
     * Get product price for a specific location and currency
     * 
     * @param productId The product ID
     * @param locationCode Location code
     * @param currencyCode Currency code
     * @return The matching product price
     */
    Optional<ProductPrice> getProductPrice(String productId, String locationCode, String currencyCode);
} 