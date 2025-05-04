package com.handmade.ecommerce.customerportal.controller;

import com.handmade.ecommerce.customerportal.model.ProductView;
import com.handmade.ecommerce.customerportal.service.ProductQueryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

/**
 * REST controller for optimized product queries.
 * This controller demonstrates the use of the optimized query service for better performance.
 */
@RestController
@RequestMapping("/api/v1/products/optimized")
public class OptimizedProductController {

    private final ProductQueryService productQueryService;

    @Autowired
    public OptimizedProductController(ProductQueryService productQueryService) {
        this.productQueryService = productQueryService;
    }

    /**
     * Search for products with optimized query handling and caching
     */
    @GetMapping("/search")
    public ResponseEntity<List<ProductView>> searchProducts(
            @RequestHeader("X-Tenant-ID") String tenantId,
            @RequestParam(required = false) String searchTerm,
            @RequestParam(required = false) BigDecimal minPrice,
            @RequestParam(required = false) BigDecimal maxPrice,
            @RequestParam(required = false, defaultValue = "newest") String sortBy,
            @RequestParam(required = false, defaultValue = "20") int limit,
            @RequestParam(required = false, defaultValue = "0") int offset,
            @RequestParam(required = false, defaultValue = "true") boolean useCache) {
        
        try {
            List<ProductView> products = productQueryService.searchProductsOptimized(
                    tenantId, searchTerm, minPrice, maxPrice, sortBy, limit, offset, useCache);
            
            // Track this search query asynchronously for analytics
            if (searchTerm != null && !searchTerm.isEmpty()) {
                // Implementation could log search terms for popularity tracking
            }
            
            return ResponseEntity.ok(products != null ? products : Collections.emptyList());
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, 
                "Error searching products: " + e.getMessage(), e);
        }
    }

    /**
     * Get products by category with async processing for improved response time
     */
    @GetMapping("/category/{categoryId}")
    public CompletableFuture<ResponseEntity<List<ProductView>>> getProductsByCategory(
            @RequestHeader("X-Tenant-ID") String tenantId,
            @PathVariable String categoryId,
            @RequestParam(required = false, defaultValue = "newest") String sortBy,
            @RequestParam(required = false, defaultValue = "20") int limit,
            @RequestParam(required = false, defaultValue = "0") int offset) {
        
        return productQueryService.getProductsByCategoryAsync(tenantId, categoryId, sortBy, limit, offset)
                .thenApply(products -> ResponseEntity.ok(products != null ? products : Collections.emptyList()))
                .exceptionally(ex -> {
                    throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, 
                        "Error fetching products by category: " + ex.getMessage(), ex);
                });
    }

    /**
     * Get featured products with caching for better performance
     */
    @GetMapping("/featured")
    public ResponseEntity<List<ProductView>> getFeaturedProducts(
            @RequestHeader("X-Tenant-ID") String tenantId,
            @RequestParam(required = false, defaultValue = "10") int limit) {
        
        try {
            List<ProductView> products = productQueryService.getCachedFeaturedProducts(tenantId, limit);
            return ResponseEntity.ok(products != null ? products : Collections.emptyList());
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR,
                "Error fetching featured products: " + e.getMessage(), e);
        }
    }

    /**
     * Get product by ID with cache lookup
     */
    @GetMapping("/{productId}")
    public ResponseEntity<ProductView> getProductById(
            @RequestHeader("X-Tenant-ID") String tenantId,
            @PathVariable String productId,
            @RequestParam(required = false) String customerId) {
        
        try {
            List<ProductView> products = productQueryService.getProductsByIds(tenantId, List.of(productId));
            
            if (products == null || products.isEmpty()) {
                return ResponseEntity.notFound().build();
            }
            
            // Asynchronously track this product view if customer is provided
            if (customerId != null && !customerId.isEmpty()) {
                productQueryService.trackProductView(tenantId, productId, customerId);
            }
            
            return ResponseEntity.ok(products.get(0));
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, 
                "Error fetching product: " + e.getMessage(), e);
        }
    }
    
    /**
     * Get similar products with optimized similarity algorithm
     */
    @GetMapping("/{productId}/similar")
    public ResponseEntity<List<ProductView>> getSimilarProducts(
            @RequestHeader("X-Tenant-ID") String tenantId,
            @PathVariable String productId,
            @RequestParam(required = false, defaultValue = "5") int limit) {
        
        try {
            // Use the optimized similarity algorithm
            List<ProductView> similarProducts = productQueryService.findSimilarProducts(tenantId, productId, limit);
            return ResponseEntity.ok(similarProducts != null ? similarProducts : Collections.emptyList());
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, 
                "Error finding similar products: " + e.getMessage(), e);
        }
    }
    
    /**
     * Get recently viewed products for a customer
     */
    @GetMapping("/recent")
    public ResponseEntity<List<ProductView>> getRecentlyViewedProducts(
            @RequestHeader("X-Tenant-ID") String tenantId,
            @RequestParam String customerId,
            @RequestParam(required = false, defaultValue = "10") int limit) {
        
        try {
            List<ProductView> products = productQueryService.getRecentlyViewedProducts(tenantId, customerId, limit);
            return ResponseEntity.ok(products != null ? products : Collections.emptyList());
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, 
                "Error fetching recently viewed products: " + e.getMessage(), e);
        }
    }
    
    /**
     * Bulk fetch products by IDs
     */
    @PostMapping("/batch")
    public ResponseEntity<List<ProductView>> getProductsByIds(
            @RequestHeader("X-Tenant-ID") String tenantId,
            @RequestBody List<String> productIds) {
        
        try {
            if (productIds == null || productIds.isEmpty()) {
                return ResponseEntity.ok(Collections.emptyList());
            }
            
            List<ProductView> products = productQueryService.getProductsByIds(tenantId, productIds);
            return ResponseEntity.ok(products != null ? products : Collections.emptyList());
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, 
                "Error fetching products batch: " + e.getMessage(), e);
        }
    }
    
    /**
     * Preload products into the cache for anticipated high traffic
     */
    @PostMapping("/preload")
    public ResponseEntity<Void> preloadProducts(
            @RequestHeader("X-Tenant-ID") String tenantId,
            @RequestBody List<String> productIds) {
        
        try {
            productQueryService.preloadProducts(tenantId, productIds);
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, 
                "Error preloading products: " + e.getMessage(), e);
        }
    }
    
    /**
     * Invalidate cache entries
     */
    @DeleteMapping("/cache")
    public ResponseEntity<Void> invalidateCache(
            @RequestHeader("X-Tenant-ID") String tenantId,
            @RequestParam(required = false) String productId) {
        
        try {
            productQueryService.invalidateCache(tenantId, productId);
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, 
                "Error invalidating cache: " + e.getMessage(), e);
        }
    }
} 