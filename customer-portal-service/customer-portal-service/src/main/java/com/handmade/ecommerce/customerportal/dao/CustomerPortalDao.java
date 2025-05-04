package com.handmade.ecommerce.customerportal.dao;

import com.handmade.ecommerce.customerportal.model.*;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.math.BigDecimal;
import java.util.List;

/**
 * Data Access Object for the Customer Portal.
 * This interface maps to the CustomerPortalQueries.xml file.
 */
@Mapper
public interface CustomerPortalDao {
    
    /**
     * Search for products with filters
     */
    List<ProductView> searchProducts(
        @Param("tenantId") String tenantId,
        @Param("searchTerm") String searchTerm,
        @Param("minPrice") BigDecimal minPrice,
        @Param("maxPrice") BigDecimal maxPrice,
        @Param("sortBy") String sortBy,
        @Param("limit") int limit,
        @Param("offset") int offset
    );
    
    /**
     * Get products by category
     */
    List<ProductView> getProductsByCategory(
        @Param("tenantId") String tenantId,
        @Param("categoryId") String categoryId,
        @Param("sortBy") String sortBy,
        @Param("limit") int limit,
        @Param("offset") int offset
    );
    
    /**
     * Get products by artisan
     */
    List<ProductView> getProductsByArtisan(
        @Param("tenantId") String tenantId,
        @Param("artisanId") String artisanId,
        @Param("limit") int limit,
        @Param("offset") int offset
    );
    
    /**
     * Get detailed product information
     */
    ProductDetail getProductDetails(
        @Param("tenantId") String tenantId,
        @Param("productId") String productId
    );
    
    /**
     * Get artisan profile information
     */
    ArtisanProfile getArtisanProfile(
        @Param("tenantId") String tenantId,
        @Param("artisanId") String artisanId
    );
    
    /**
     * Get customer order history
     */
    List<CustomerOrder> getCustomerOrders(
        @Param("tenantId") String tenantId,
        @Param("customerId") String customerId,
        @Param("limit") int limit,
        @Param("offset") int offset
    );
    
    /**
     * Get featured products
     */
    List<ProductView> getFeaturedProducts(
        @Param("tenantId") String tenantId,
        @Param("limit") int limit
    );
    
    /**
     * Get categories with product counts
     */
    List<Category> getCategories(
        @Param("tenantId") String tenantId
    );
    
    /**
     * Get related products for a specific product
     */
    List<ProductView> getRelatedProducts(
        @Param("tenantId") String tenantId,
        @Param("productId") String productId,
        @Param("limit") int limit
    );
} 