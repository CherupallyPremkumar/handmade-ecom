package com.handmade.ecommerce.customerportal.service;

import com.handmade.ecommerce.customerportal.model.ProductView;
import java.math.BigDecimal;
import java.util.List;
import java.util.concurrent.CompletableFuture;

/**
 * High-performance query service specifically optimized for product retrieval operations.
 * This service implements various strategies for improving performance including:
 * - Caching
 * - Asynchronous loading
 * - Selective data fetching
 * - Result batching
 */
public interface ProductQueryService {
    
    /**
     * Performs a high-performance search for products with various optimizations
     * 
     * @param tenantId Tenant identifier
     * @param searchTerm Search term to filter products
     * @param minPrice Minimum price filter
     * @param maxPrice Maximum price filter
     * @param sortBy Sorting criteria
     * @param limit Maximum number of results
     * @param offset Pagination offset
     * @param useCache Whether to use cache for this query
     * @return List of matching products
     */
    List<ProductView> searchProductsOptimized(String tenantId, String searchTerm, 
                                            BigDecimal minPrice, BigDecimal maxPrice,
                                            String sortBy, int limit, int offset, 
                                            boolean useCache);
    
    /**
     * Asynchronously fetch products by category for improved performance
     * 
     * @param tenantId Tenant identifier
     * @param categoryId Category identifier
     * @param sortBy Sorting criteria
     * @param limit Maximum number of results
     * @param offset Pagination offset
     * @return CompletableFuture containing list of products
     */
    CompletableFuture<List<ProductView>> getProductsByCategoryAsync(String tenantId, 
                                                                  String categoryId,
                                                                  String sortBy, 
                                                                  int limit, 
                                                                  int offset);
    
    /**
     * Get featured products with caching
     * 
     * @param tenantId Tenant identifier
     * @param limit Maximum number of results
     * @return List of featured products from cache or database
     */
    List<ProductView> getCachedFeaturedProducts(String tenantId, int limit);
    
    /**
     * Pre-load and cache product data for improved performance
     * 
     * @param tenantId Tenant identifier
     * @param productIds List of product IDs to preload
     */
    void preloadProducts(String tenantId, List<String> productIds);
    
    /**
     * Clear cached product data
     * 
     * @param tenantId Tenant identifier
     * @param productId Product identifier, or null to clear all for tenant
     */
    void invalidateCache(String tenantId, String productId);
    
    /**
     * Get recently viewed products for a customer with performance optimizations
     * 
     * @param tenantId Tenant identifier
     * @param customerId Customer identifier
     * @param limit Maximum number of results
     * @return List of recently viewed products
     */
    List<ProductView> getRecentlyViewedProducts(String tenantId, String customerId, int limit);
    
    /**
     * Track product view for analytics and recommendations
     * Uses a separate thread pool to avoid blocking the main request thread
     * 
     * @param tenantId Tenant identifier
     * @param productId Product identifier
     * @param customerId Customer identifier
     */
    void trackProductView(String tenantId, String productId, String customerId);
    
    /**
     * Bulk fetch products by IDs with a single query
     * 
     * @param tenantId Tenant identifier
     * @param productIds List of product IDs to fetch
     * @return List of product views
     */
    List<ProductView> getProductsByIds(String tenantId, List<String> productIds);
    
    /**
     * Find similar products using optimized algorithm
     * 
     * @param tenantId Tenant identifier
     * @param productId Product identifier
     * @param limit Maximum number of results
     * @return List of similar products
     */
    List<ProductView> findSimilarProducts(String tenantId, String productId, int limit);
} 