package com.handmade.ecommerce.tenant.configuration.controller;

import com.handmade.ecommerce.common.exception.ResourceNotFoundException;
import com.handmade.ecommerce.tenant.model.TenantConfiguration;
import com.handmade.ecommerce.tenant.model.service.TenantConfigurationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * REST controller for tenant configuration management
 */
@Slf4j
@RestController
@RequestMapping("/api/tenants/{tenantId}/configurations")
@RequiredArgsConstructor
public class TenantConfigurationController {
    
    private final TenantConfigurationService configurationService;
    
    /**
     * Get all configurations for a tenant
     */
    @GetMapping
    public ResponseEntity<List<TenantConfiguration>> getAllConfigurations(@PathVariable String tenantId) {
        log.info("REST request to get all configurations for tenant: {}", tenantId);
        List<TenantConfiguration> configurations = configurationService.getAllConfigurations(tenantId);
        return ResponseEntity.ok(configurations);
    }
    
    /**
     * Get all configurations as a map
     */
    @GetMapping("/map")
    public ResponseEntity<Map<String, String>> getConfigurationsAsMap(@PathVariable String tenantId) {
        log.info("REST request to get configurations as map for tenant: {}", tenantId);
        Map<String, String> configurations = configurationService.getConfigurationsAsMap(tenantId);
        return ResponseEntity.ok(configurations);
    }
    
    /**
     * Get configurations by category
     */
    @GetMapping("/category/{category}")
    public ResponseEntity<List<TenantConfiguration>> getConfigurationsByCategory(
            @PathVariable String tenantId, 
            @PathVariable String category) {
        log.info("REST request to get configurations for tenant: {} in category: {}", tenantId, category);
        List<TenantConfiguration> configurations = configurationService.getConfigurationsByCategory(tenantId, category);
        return ResponseEntity.ok(configurations);
    }
    
    /**
     * Get a configuration by key
     */
    @GetMapping("/{key}")
    public ResponseEntity<String> getConfigurationValue(
            @PathVariable String tenantId,
            @PathVariable String key) {
        log.info("REST request to get configuration for tenant: {} with key: {}", tenantId, key);
        String value = configurationService.getConfigValue(tenantId, key)
                .orElseThrow(() -> new ResourceNotFoundException("Configuration", tenantId + ":" + key));
        return ResponseEntity.ok(value);
    }
    
    /**
     * Create a new configuration
     */
    @PostMapping
    public ResponseEntity<TenantConfiguration> createConfiguration(
            @PathVariable String tenantId,
            @RequestBody TenantConfiguration configuration) {
        log.info("REST request to create configuration for tenant: {} with key: {}", 
                tenantId, configuration.getConfigKey());
        
        // Set the tenant ID from the path variable
        configuration.setTenantId(tenantId);
        
        TenantConfiguration createdConfiguration = configurationService.createConfiguration(configuration);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdConfiguration);
    }
    
    /**
     * Create a simple configuration (key-value only)
     */
    @PostMapping("/{key}")
    public ResponseEntity<TenantConfiguration> createSimpleConfiguration(
            @PathVariable String tenantId,
            @PathVariable String key,
            @RequestBody String value) {
        log.info("REST request to create simple configuration for tenant: {} with key: {}", tenantId, key);
        TenantConfiguration createdConfiguration = configurationService.createConfiguration(tenantId, key, value);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdConfiguration);
    }
    
    /**
     * Update a configuration
     */
    @PutMapping("/{key}")
    public ResponseEntity<TenantConfiguration> updateConfiguration(
            @PathVariable String tenantId,
            @PathVariable String key,
            @RequestBody String value) {
        log.info("REST request to update configuration for tenant: {} with key: {}", tenantId, key);
        TenantConfiguration updatedConfiguration = configurationService.updateConfiguration(tenantId, key, value);
        return ResponseEntity.ok(updatedConfiguration);
    }
    
    /**
     * Delete a configuration
     */
    @DeleteMapping("/{key}")
    public ResponseEntity<Void> deleteConfiguration(
            @PathVariable String tenantId,
            @PathVariable String key) {
        log.info("REST request to delete configuration for tenant: {} with key: {}", tenantId, key);
        configurationService.deleteConfiguration(tenantId, key);
        return ResponseEntity.noContent().build();
    }
    
    /**
     * Check if a configuration exists
     */
    @GetMapping("/{key}/exists")
    public ResponseEntity<Boolean> hasConfiguration(
            @PathVariable String tenantId,
            @PathVariable String key) {
        log.info("REST request to check if configuration exists for tenant: {} with key: {}", tenantId, key);
        boolean exists = configurationService.hasConfiguration(tenantId, key);
        return ResponseEntity.ok(exists);
    }
} 