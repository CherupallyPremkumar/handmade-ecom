package com.handmade.ecommerce.customerportal.service;

import com.handmade.ecommerce.customerportal.model.*;
import java.math.BigDecimal;
import java.util.List;

/**
 * Service interface for customer portal operations.
 * Provides methods for customers to browse products, view details, and manage orders.
 */
public interface CustomerPortalService {
    
    /**
     * Search for products with various filters
     * 
     * @param tenantId Tenant identifier
     * @param searchTerm Search term to filter products
     * @param minPrice Minimum price filter
     * @param maxPrice Maximum price filter
     * @param sortBy Sorting criteria (price_asc, price_desc, newest, popular)
     * @param limit Maximum number of results
     * @param offset Pagination offset
     * @return List of matching products
     */
    List<ProductView> searchProducts(String tenantId, String searchTerm, 
                                    BigDecimal minPrice, BigDecimal maxPrice,
                                    String sortBy, int limit, int offset);
    
    /**
     * Get products by category
     * 
     * @param tenantId Tenant identifier
     * @param categoryId Category identifier
     * @param sortBy Sorting criteria
     * @param limit Maximum number of results
     * @param offset Pagination offset
     * @return List of products in the category
     */
    List<ProductView> getProductsByCategory(String tenantId, String categoryId,
                                           String sortBy, int limit, int offset);
    
    /**
     * Get products by artisan
     * 
     * @param tenantId Tenant identifier
     * @param artisanId Artisan identifier
     * @param limit Maximum number of results
     * @param offset Pagination offset
     * @return List of products by the artisan
     */
    List<ProductView> getProductsByArtisan(String tenantId, String artisanId,
                                          int limit, int offset);
    
    /**
     * Get detailed product information
     * 
     * @param tenantId Tenant identifier
     * @param productId Product identifier
     * @return Detailed product information
     */
    ProductDetail getProductDetails(String tenantId, String productId);
    
    /**
     * Get artisan profile information
     * 
     * @param tenantId Tenant identifier
     * @param artisanId Artisan identifier
     * @return Artisan profile
     */
    ArtisanProfile getArtisanProfile(String tenantId, String artisanId);
    
    /**
     * Get customer order history
     * 
     * @param tenantId Tenant identifier
     * @param customerId Customer identifier
     * @param limit Maximum number of results
     * @param offset Pagination offset
     * @return List of customer orders
     */
    List<CustomerOrder> getCustomerOrders(String tenantId, String customerId,
                                         int limit, int offset);
    
    /**
     * Get featured products
     * 
     * @param tenantId Tenant identifier
     * @param limit Maximum number of results
     * @return List of featured products
     */
    List<ProductView> getFeaturedProducts(String tenantId, int limit);
    
    /**
     * Get categories with product counts
     * 
     * @param tenantId Tenant identifier
     * @return List of categories
     */
    List<Category> getCategories(String tenantId);
    
    /**
     * Get related products for a specific product
     * 
     * @param tenantId Tenant identifier
     * @param productId Product identifier
     * @param limit Maximum number of results
     * @return List of related products
     */
    List<ProductView> getRelatedProducts(String tenantId, String productId, int limit);
} 