package com.handmade.ecommerce.tenant.model;

/**
 * ThreadLocal based utility class to store and access the current tenant ID
 * throughout the application. This allows services to access the tenant
 * context without passing it through method calls.
 */
public class TenantContext {
    private static final ThreadLocal<String> currentTenant = new ThreadLocal<>();
    
    /**
     * Get the current tenant ID
     * @return the current tenant ID
     */
    public static String getCurrentTenant() {
        return currentTenant.get();
    }
    
    /**
     * Set the current tenant ID
     * @param tenantId the tenant ID to set
     */
    public static void setCurrentTenant(String tenantId) {
        currentTenant.set(tenantId);
    }
    
    /**
     * Clear the current tenant ID
     */
    public static void clear() {
        currentTenant.remove();
    }
} 