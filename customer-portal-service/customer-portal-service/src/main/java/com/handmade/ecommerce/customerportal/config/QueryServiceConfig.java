package com.handmade.ecommerce.customerportal.config;

import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.caffeine.CaffeineCacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import com.github.benmanes.caffeine.cache.Caffeine;

import java.util.Arrays;
import java.util.concurrent.Executor;
import java.util.concurrent.TimeUnit;

/**
 * Configuration class for query service performance optimizations
 */
@Configuration
@EnableCaching
@EnableAsync
public class QueryServiceConfig {

    /**
     * Configure the cache manager using Caffeine for high-performance caching
     */
    @Bean
    @Primary
    public CacheManager cacheManager() {
        CaffeineCacheManager cacheManager = new CaffeineCacheManager();
        
        // Configure default cache settings
        cacheManager.setCaffeine(caffeineConfig());
        
        // Set up specific named caches with custom configurations
        cacheManager.setCacheNames(Arrays.asList(
                "featuredProductsCache",   // Featured products (longer TTL)
                "productCache",            // Individual product cache
                "categoryCache",           // Categories (rarely change)
                "searchResultsCache"       // Search results (shorter TTL)
        ));
        
        return cacheManager;
    }
    
    /**
     * Configure a dedicated thread pool for async product queries
     */
    @Bean(name = "queryTaskExecutor")
    public Executor taskExecutor() {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.setCorePoolSize(4);                // Base number of threads
        executor.setMaxPoolSize(10);                // Max threads during high load
        executor.setQueueCapacity(25);              // Queue size before rejecting
        executor.setThreadNamePrefix("QueryTask-"); // Thread naming for debugging
        executor.setWaitForTasksToCompleteOnShutdown(true);  // Clean shutdown
        executor.setAwaitTerminationSeconds(60);    // Wait up to 60 seconds on shutdown
        executor.initialize();
        return executor;
    }
    
    /**
     * Configure a separate thread pool specifically for tracking and analytics
     * to avoid blocking the main query executor
     */
    @Bean(name = "analyticsTaskExecutor")
    public Executor analyticsTaskExecutor() {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.setCorePoolSize(2);
        executor.setMaxPoolSize(5);
        executor.setQueueCapacity(100);             // Larger queue for analytics
        executor.setThreadNamePrefix("Analytics-");
        executor.setRejectedExecutionHandler((r, e) -> {
            // Just log and discard - analytics tasks are non-critical
            System.err.println("Analytics task rejected: queue full");
        });
        executor.initialize();
        return executor;
    }
    
    /**
     * Default Caffeine cache configuration
     */
    @Bean
    public Caffeine<Object, Object> caffeineConfig() {
        return Caffeine.newBuilder()
                .expireAfterWrite(5, TimeUnit.MINUTES)
                .maximumSize(1000)
                .recordStats();
    }
    
    /**
     * Special cache configuration for stable data like categories
     */
    @Bean(name = "stableDataCaffeineConfig")
    public Caffeine<Object, Object> stableDataCaffeineConfig() {
        return Caffeine.newBuilder()
                .expireAfterWrite(60, TimeUnit.MINUTES)
                .maximumSize(100)
                .recordStats();
    }
    
    /**
     * Custom cache manager specifically for longer-lived stable data
     */
    @Bean(name = "stableDataCacheManager")
    public CacheManager stableDataCacheManager() {
        CaffeineCacheManager cacheManager = new CaffeineCacheManager();
        cacheManager.setCaffeine(stableDataCaffeineConfig());
        cacheManager.setCacheNames(Arrays.asList("categoryCache", "staticDataCache"));
        return cacheManager;
    }
} 