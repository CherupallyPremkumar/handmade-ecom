package com.handmade.ecommerce.tenant.model.service;

import com.handmade.ecommerce.tenant.model.Tenant;

import java.util.List;
import java.util.Optional;

/**
 * Service interface for managing tenants.
 * This service handles the full tenant lifecycle including
 * creation, activation, suspension, and deletion.
 */
public interface TenantService {
    
    /**
     * Create a new tenant
     * 
     * @param tenant the tenant to create
     * @return the created tenant
     */
    Tenant createTenant(Tenant tenant);
    
    /**
     * Get a tenant by ID
     * 
     * @param tenantId the tenant ID
     * @return the tenant if found
     */
    Optional<Tenant> getTenantById(String tenantId);
    
    /**
     * Get all tenants
     * 
     * @return list of all tenants
     */
    List<Tenant> getAllTenants();
    
    /**
     * Get tenants by status
     * 
     * @param status the tenant status
     * @return list of tenants with the given status
     */
    List<Tenant> getTenantsByStatus(String status);
    
    /**
     * Assign a tenant to be onboarded
     * 
     * @param tenantId the tenant ID
     * @return the updated tenant
     */
    Tenant assignTenant(String tenantId);
    
    /**
     * Approve a tenant after setup is complete
     * 
     * @param tenantId the tenant ID
     * @return the updated tenant
     */
    Tenant approveTenant(String tenantId);
    
    /**
     * Activate a tenant (make it available to users)
     * 
     * @param tenantId the tenant ID
     * @return the updated tenant
     */
    Tenant activateTenant(String tenantId);
    
    /**
     * Suspend a tenant temporarily
     * 
     * @param tenantId the tenant ID
     * @param reason the suspension reason
     * @return the updated tenant
     */
    Tenant suspendTenant(String tenantId, String reason);
    
    /**
     * Reactivate a suspended tenant
     * 
     * @param tenantId the tenant ID
     * @return the updated tenant
     */
    Tenant reactivateTenant(String tenantId);
    
    /**
     * Close a tenant permanently
     * 
     * @param tenantId the tenant ID
     * @param reason the closure reason
     * @return the updated tenant
     */
    Tenant closeTenant(String tenantId, String reason);
    
    /**
     * Check if a tenant is active
     * 
     * @param tenantId the tenant ID
     * @return true if the tenant is active
     */
    boolean isTenantActive(String tenantId);
    
    /**
     * Get tenant by name
     * 
     * @param tenantName the tenant name
     * @return the tenant if found
     */
    Optional<Tenant> getTenantByName(String tenantName);
} 