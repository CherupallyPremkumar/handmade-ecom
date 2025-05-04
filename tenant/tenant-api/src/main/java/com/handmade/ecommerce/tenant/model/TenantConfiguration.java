package com.handmade.ecommerce.tenant.model;

import jakarta.persistence.*;
import org.chenile.jpautils.entity.AbstractJpaEntity;

/**
 * Entity class representing tenant-specific configuration.
 * This allows each tenant to have its own customized settings.
 */
@Entity
@Table(name = "hm_tenant_configuration")
public class TenantConfiguration extends AbstractJpaEntity {
    
    @Column(name = "tenant_id", nullable = false)
    private String tenantId;
    
    @Column(name = "config_key", nullable = false)
    private String configKey;
    
    @Column(name = "config_value", columnDefinition = "TEXT")
    private String configValue;
    
    @Column(name = "data_type")
    private String dataType;
    
    @Column(name = "description")
    private String description;
    
    @Column(name = "is_encrypted")
    private boolean encrypted;
    
    @Column(name = "is_active", nullable = false)
    private boolean active = true;
    
    @Column(name = "category")
    private String category;
    
    /**
     * Default constructor required by JPA
     */
    public TenantConfiguration() {
    }
    
    /**
     * Constructor with essential fields
     */
    public TenantConfiguration(String tenantId, String configKey, String configValue) {
        this.tenantId = tenantId;
        this.configKey = configKey;
        this.configValue = configValue;
        this.dataType = "STRING";
    }
    
    /**
     * Full constructor
     */
    public TenantConfiguration(String tenantId, String configKey, String configValue, 
                             String dataType, String description, boolean encrypted,
                             String category) {
        this.tenantId = tenantId;
        this.configKey = configKey;
        this.configValue = configValue;
        this.dataType = dataType;
        this.description = description;
        this.encrypted = encrypted;
        this.category = category;
    }

    // Getters and Setters
    
    public String getTenantId() {
        return tenantId;
    }

    public void setTenantId(String tenantId) {
        this.tenantId = tenantId;
    }

    public String getConfigKey() {
        return configKey;
    }

    public void setConfigKey(String configKey) {
        this.configKey = configKey;
    }

    public String getConfigValue() {
        return configValue;
    }

    public void setConfigValue(String configValue) {
        this.configValue = configValue;
    }

    public String getDataType() {
        return dataType;
    }

    public void setDataType(String dataType) {
        this.dataType = dataType;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public boolean isEncrypted() {
        return encrypted;
    }

    public void setEncrypted(boolean encrypted) {
        this.encrypted = encrypted;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }
} 