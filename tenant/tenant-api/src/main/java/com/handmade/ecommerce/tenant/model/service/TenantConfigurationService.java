package com.handmade.ecommerce.tenant.model.service;

import com.handmade.ecommerce.tenant.model.TenantConfiguration;

import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * Service interface for managing tenant configurations.
 * This service handles creating, retrieving, updating, and deleting
 * tenant-specific configuration settings.
 */
public interface TenantConfigurationService {
    
    /**
     * Get a configuration value for a tenant
     * 
     * @param tenantId the tenant ID
     * @param key the configuration key
     * @return the configuration value if found
     */
    Optional<String> getConfigValue(String tenantId, String key);
    
    /**
     * Get a configuration value with a default fallback
     * 
     * @param tenantId the tenant ID
     * @param key the configuration key
     * @param defaultValue the default value if not found
     * @return the configuration value or default
     */
    String getConfigValueOrDefault(String tenantId, String key, String defaultValue);
    
    /**
     * Get all configurations for a tenant
     * 
     * @param tenantId the tenant ID
     * @return list of tenant configurations
     */
    List<TenantConfiguration> getAllConfigurations(String tenantId);
    
    /**
     * Get configurations by category
     * 
     * @param tenantId the tenant ID
     * @param category the configuration category
     * @return list of tenant configurations for the category
     */
    List<TenantConfiguration> getConfigurationsByCategory(String tenantId, String category);
    
    /**
     * Get all configurations as a map
     * 
     * @param tenantId the tenant ID
     * @return map of configuration key to value
     */
    Map<String, String> getConfigurationsAsMap(String tenantId);
    
    /**
     * Create a new configuration
     * 
     * @param tenantId the tenant ID
     * @param key the configuration key
     * @param value the configuration value
     * @return the created configuration
     */
    TenantConfiguration createConfiguration(String tenantId, String key, String value);
    
    /**
     * Create a new configuration with metadata
     * 
     * @param configuration the configuration to create
     * @return the created configuration
     */
    TenantConfiguration createConfiguration(TenantConfiguration configuration);
    
    /**
     * Update a configuration
     * 
     * @param tenantId the tenant ID
     * @param key the configuration key
     * @param value the new configuration value
     * @return the updated configuration
     */
    TenantConfiguration updateConfiguration(String tenantId, String key, String value);
    
    /**
     * Delete a configuration
     * 
     * @param tenantId the tenant ID
     * @param key the configuration key
     */
    void deleteConfiguration(String tenantId, String key);
    
    /**
     * Check if a configuration exists
     * 
     * @param tenantId the tenant ID
     * @param key the configuration key
     * @return true if the configuration exists
     */
    boolean hasConfiguration(String tenantId, String key);
} 