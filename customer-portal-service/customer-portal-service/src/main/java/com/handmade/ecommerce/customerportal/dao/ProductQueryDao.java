package com.handmade.ecommerce.customerportal.dao;

import com.handmade.ecommerce.customerportal.model.ProductView;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.math.BigDecimal;
import java.util.List;

/**
 * Specialized DAO for optimized product queries.
 * This interface contains methods specifically designed for high-performance operations.
 */
@Mapper
public interface ProductQueryDao {
    
    /**
     * Find products using non-text search (more efficient with proper indices)
     */
    List<ProductView> findProductsNonTextSearch(
        @Param("tenantId") String tenantId,
        @Param("minPrice") BigDecimal minPrice,
        @Param("maxPrice") BigDecimal maxPrice,
        @Param("sortBy") String sortBy,
        @Param("limit") int limit,
        @Param("offset") int offset
    );
    
    /**
     * Get products by IDs in a single query for batching
     */
    List<ProductView> getProductsByIds(
        @Param("tenantId") String tenantId,
        @Param("productIds") List<String> productIds
    );
    
    /**
     * Get recently viewed product IDs for a customer 
     */
    List<String> getRecentlyViewedProductIds(
        @Param("tenantId") String tenantId,
        @Param("customerId") String customerId,
        @Param("limit") int limit
    );
    
    /**
     * Track a product view for analytics and recommendations
     */
    void trackProductView(
        @Param("tenantId") String tenantId,
        @Param("productId") String productId,
        @Param("customerId") String customerId
    );
    
    /**
     * Get product counts by category for efficient dashboard display
     */
    List<Object[]> getProductCountsByCategory(
        @Param("tenantId") String tenantId
    );
    
    /**
     * Find similar products using more efficient algorithm than MyBatis JOIN
     */
    List<ProductView> findSimilarProducts(
        @Param("tenantId") String tenantId,
        @Param("productId") String productId,
        @Param("limit") int limit
    );
} 