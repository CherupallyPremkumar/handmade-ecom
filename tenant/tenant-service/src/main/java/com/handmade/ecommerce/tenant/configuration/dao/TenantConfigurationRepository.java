package com.handmade.ecommerce.tenant.configuration.dao;

import com.handmade.ecommerce.tenant.model.TenantConfiguration;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Repository for TenantConfiguration entity
 */
@Repository
public interface TenantConfigurationRepository extends JpaRepository<TenantConfiguration, String> {

    /**
     * Find a configuration by tenant ID and key
     * @param tenantId the tenant ID
     * @param key the configuration key
     * @return the configuration if found
     */
    @Query("SELECT c FROM TenantConfiguration c WHERE c.tenantId = :tenantId AND c.configKey = :key AND c.active = true")
    Optional<TenantConfiguration> findByTenantIdAndKey(@Param("tenantId") String tenantId, @Param("key") String key);

    /**
     * Find all configurations for a tenant
     * @param tenantId the tenant ID
     * @return list of active configurations
     */
    @Query("SELECT c FROM TenantConfiguration c WHERE c.tenantId = :tenantId AND c.active = true")
    List<TenantConfiguration> findAllByTenantId(@Param("tenantId") String tenantId);

    /**
     * Find configurations by tenant ID and category
     * @param tenantId the tenant ID
     * @param category the configuration category
     * @return list of configurations
     */
    @Query("SELECT c FROM TenantConfiguration c WHERE c.tenantId = :tenantId AND c.category = :category AND c.active = true")
    List<TenantConfiguration> findByTenantIdAndCategory(@Param("tenantId") String tenantId, @Param("category") String category);

    /**
     * Check if a configuration exists
     * @param tenantId the tenant ID
     * @param key the configuration key
     * @return true if exists
     */
    @Query("SELECT COUNT(c) > 0 FROM TenantConfiguration c WHERE c.tenantId = :tenantId AND c.configKey = :key AND c.active = true")
    boolean existsByTenantIdAndKey(@Param("tenantId") String tenantId, @Param("key") String key);
} 