package com.handmade.ecommerce.customerportal.service.impl;

import com.handmade.ecommerce.customerportal.dao.CustomerPortalDao;
import com.handmade.ecommerce.customerportal.dao.ProductQueryDao;
import com.handmade.ecommerce.customerportal.model.ProductView;
import com.handmade.ecommerce.customerportal.service.ProductQueryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Executor;
import java.util.stream.Collectors;

/**
 * Implementation of ProductQueryService with performance optimizations
 */
@Service
public class ProductQueryServiceImpl implements ProductQueryService {

    private final CustomerPortalDao customerPortalDao;
    private final ProductQueryDao productQueryDao;
    private final Executor queryTaskExecutor;
    private final Executor analyticsTaskExecutor;
    
    // In-memory cache for high-frequency data
    private final Map<String, Map<String, Object>> localCache = new ConcurrentHashMap<>();
    
    @Autowired
    public ProductQueryServiceImpl(CustomerPortalDao customerPortalDao, 
                                  ProductQueryDao productQueryDao,
                                  @Qualifier("queryTaskExecutor") Executor queryTaskExecutor,
                                  @Qualifier("analyticsTaskExecutor") Executor analyticsTaskExecutor) {
        this.customerPortalDao = customerPortalDao;
        this.productQueryDao = productQueryDao;
        this.queryTaskExecutor = queryTaskExecutor;
        this.analyticsTaskExecutor = analyticsTaskExecutor;
    }

    @Override
    public List<ProductView> searchProductsOptimized(String tenantId, String searchTerm, 
                                                  BigDecimal minPrice, BigDecimal maxPrice,
                                                  String sortBy, int limit, int offset, 
                                                  boolean useCache) {
        String cacheKey = buildSearchCacheKey(tenantId, searchTerm, minPrice, maxPrice, sortBy, limit, offset);
        
        if (useCache) {
            @SuppressWarnings("unchecked")
            List<ProductView> cachedResult = (List<ProductView>) getFromLocalCache(tenantId, cacheKey);
            if (cachedResult != null) {
                return cachedResult;
            }
        }
        
        // Use more optimized query if possible
        List<ProductView> result;
        if (searchTerm == null || searchTerm.isEmpty()) {
            // Simple non-text search can use index-optimized query
            result = productQueryDao.findProductsNonTextSearch(tenantId, minPrice, maxPrice, sortBy, limit, offset);
        } else {
            // Text search might use full-text search capabilities
            result = customerPortalDao.searchProducts(tenantId, searchTerm, minPrice, maxPrice, sortBy, limit, offset);
        }
        
        if (useCache && result != null) {
            putInLocalCache(tenantId, cacheKey, result);
        }
        
        return result;
    }

    @Async("queryTaskExecutor")
    @Override
    public CompletableFuture<List<ProductView>> getProductsByCategoryAsync(String tenantId, 
                                                                        String categoryId,
                                                                        String sortBy, 
                                                                        int limit, 
                                                                        int offset) {
        return CompletableFuture.supplyAsync(() -> 
            customerPortalDao.getProductsByCategory(tenantId, categoryId, sortBy, limit, offset),
            queryTaskExecutor
        );
    }

    @Cacheable(value = "featuredProductsCache", key = "#tenantId + '-' + #limit")
    @Override
    public List<ProductView> getCachedFeaturedProducts(String tenantId, int limit) {
        return customerPortalDao.getFeaturedProducts(tenantId, limit);
    }

    @Override
    public void preloadProducts(String tenantId, List<String> productIds) {
        if (productIds == null || productIds.isEmpty()) {
            return;
        }
        
        // Execute this asynchronously to not block
        CompletableFuture.runAsync(() -> {
            List<ProductView> products = productQueryDao.getProductsByIds(tenantId, productIds);
            Map<String, ProductView> productMap = products.stream()
                .collect(Collectors.toMap(ProductView::getId, p -> p, (p1, p2) -> p1));
            
            // Cache individually for faster lookups
            products.forEach(product -> 
                putInLocalCache(tenantId, "product-" + product.getId(), product));
            
            // Also cache the entire batch
            putInLocalCache(tenantId, "product-batch-" + UUID.randomUUID().toString(), productMap);
        }, queryTaskExecutor);
    }

    @CacheEvict(value = {"featuredProductsCache", "productCache"}, 
                allEntries = true, condition = "#productId == null")
    @Override
    public void invalidateCache(String tenantId, String productId) {
        if (productId != null) {
            // Remove specific product from local cache
            removeFromLocalCache(tenantId, "product-" + productId);
        } else {
            // Clear all caches for this tenant
            localCache.remove(tenantId);
        }
    }

    @Override
    public List<ProductView> getRecentlyViewedProducts(String tenantId, String customerId, int limit) {
        List<String> recentProductIds = productQueryDao.getRecentlyViewedProductIds(tenantId, customerId, limit);
        if (recentProductIds == null || recentProductIds.isEmpty()) {
            return Collections.emptyList();
        }
        
        // Try to get from cache first
        List<ProductView> result = new ArrayList<>(recentProductIds.size());
        List<String> missingProductIds = new ArrayList<>();
        
        for (String productId : recentProductIds) {
            ProductView product = (ProductView) getFromLocalCache(tenantId, "product-" + productId);
            if (product != null) {
                result.add(product);
            } else {
                missingProductIds.add(productId);
            }
        }
        
        // Fetch missing products in a single query
        if (!missingProductIds.isEmpty()) {
            List<ProductView> missingProducts = productQueryDao.getProductsByIds(tenantId, missingProductIds);
            if (missingProducts != null) {
                result.addAll(missingProducts);
                
                // Cache these for future use
                missingProducts.forEach(product -> 
                    putInLocalCache(tenantId, "product-" + product.getId(), product));
            }
        }
        
        // Sort according to the original recently viewed order
        Map<String, Integer> indexMap = new HashMap<>();
        for (int i = 0; i < recentProductIds.size(); i++) {
            indexMap.put(recentProductIds.get(i), i);
        }
        
        result.sort(Comparator.comparing(p -> indexMap.getOrDefault(p.getId(), Integer.MAX_VALUE)));
        
        return result.isEmpty() ? Collections.emptyList() : 
               result.subList(0, Math.min(result.size(), limit));
    }

    @Async("analyticsTaskExecutor")
    @Override
    public void trackProductView(String tenantId, String productId, String customerId) {
        // Execute in separate thread to not block the main request
        try {
            productQueryDao.trackProductView(tenantId, productId, customerId);
        } catch (Exception e) {
            // Log error but don't propagate - analytics should not affect user experience
            // In a real implementation, we would use a logging framework
            System.err.println("Error tracking product view: " + e.getMessage());
        }
    }

    @Override
    public List<ProductView> getProductsByIds(String tenantId, List<String> productIds) {
        if (productIds == null || productIds.isEmpty()) {
            return Collections.emptyList();
        }
        
        // Check cache first
        List<ProductView> result = new ArrayList<>(productIds.size());
        List<String> missingProductIds = new ArrayList<>();
        
        for (String productId : productIds) {
            ProductView product = (ProductView) getFromLocalCache(tenantId, "product-" + productId);
            if (product != null) {
                result.add(product);
            } else {
                missingProductIds.add(productId);
            }
        }
        
        // Fetch missing products in a batch
        if (!missingProductIds.isEmpty()) {
            List<ProductView> missingProducts = productQueryDao.getProductsByIds(tenantId, missingProductIds);
            if (missingProducts != null) {
                result.addAll(missingProducts);
                
                // Cache these for future use
                missingProducts.forEach(product -> 
                    putInLocalCache(tenantId, "product-" + product.getId(), product));
            }
        }
        
        return result;
    }
    
    @Override
    public List<ProductView> findSimilarProducts(String tenantId, String productId, int limit) {
        try {
            return productQueryDao.findSimilarProducts(tenantId, productId, limit);
        } catch (Exception e) {
            // Fallback to fetching products from the same category
            return Collections.emptyList();
        }
    }
    
    // Helper methods for local cache management
    
    private String buildSearchCacheKey(String tenantId, String searchTerm, BigDecimal minPrice, 
                                      BigDecimal maxPrice, String sortBy, int limit, int offset) {
        return String.format("search-%s-%s-%s-%s-%s-%d-%d", 
            tenantId, 
            searchTerm == null ? "" : searchTerm,
            minPrice == null ? "0" : minPrice.toString(),
            maxPrice == null ? "max" : maxPrice.toString(),
            sortBy == null ? "default" : sortBy,
            limit, 
            offset);
    }
    
    private Object getFromLocalCache(String tenantId, String key) {
        Map<String, Object> tenantCache = localCache.get(tenantId);
        if (tenantCache == null) {
            return null;
        }
        return tenantCache.get(key);
    }
    
    private void putInLocalCache(String tenantId, String key, Object value) {
        if (value == null) {
            return;
        }
        localCache.computeIfAbsent(tenantId, k -> new ConcurrentHashMap<>())
                  .put(key, value);
    }
    
    private void removeFromLocalCache(String tenantId, String key) {
        Map<String, Object> tenantCache = localCache.get(tenantId);
        if (tenantCache != null) {
            tenantCache.remove(key);
        }
    }
} 