package com.handmade.ecommerce.tenant.service.impl;

import com.handmade.ecommerce.common.exception.ResourceNotFoundException;
import com.handmade.ecommerce.tenant.configuration.dao.TenantConfigurationRepository;
import com.handmade.ecommerce.tenant.configuration.dao.TenantRepository;
import com.handmade.ecommerce.tenant.model.TenantConfiguration;
import com.handmade.ecommerce.tenant.model.service.TenantConfigurationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Implementation of TenantConfigurationService.
 * Provides methods to manage tenant-specific configurations.
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class TenantConfigurationServiceImpl implements TenantConfigurationService {

    private final TenantConfigurationRepository configurationRepository;
    private final TenantRepository tenantRepository;

    @Override
    @Transactional(readOnly = true)
    public Optional<String> getConfigValue(String tenantId, String key) {
        log.debug("Getting configuration value for tenant {} with key {}", tenantId, key);
        return configurationRepository.findByTenantIdAndKey(tenantId, key)
                .map(TenantConfiguration::getConfigValue);
    }

    @Override
    @Transactional(readOnly = true)
    public String getConfigValueOrDefault(String tenantId, String key, String defaultValue) {
        log.debug("Getting configuration value for tenant {} with key {}, defaulting to {}", tenantId, key, defaultValue);
        return getConfigValue(tenantId, key).orElse(defaultValue);
    }

    @Override
    @Transactional(readOnly = true)
    public List<TenantConfiguration> getAllConfigurations(String tenantId) {
        log.debug("Getting all configurations for tenant {}", tenantId);
        validateTenantExists(tenantId);
        return configurationRepository.findAllByTenantId(tenantId);
    }

    @Override
    @Transactional(readOnly = true)
    public List<TenantConfiguration> getConfigurationsByCategory(String tenantId, String category) {
        log.debug("Getting configurations for tenant {} in category {}", tenantId, category);
        validateTenantExists(tenantId);
        return configurationRepository.findByTenantIdAndCategory(tenantId, category);
    }

    @Override
    @Transactional(readOnly = true)
    public Map<String, String> getConfigurationsAsMap(String tenantId) {
        log.debug("Getting configurations as map for tenant {}", tenantId);
        validateTenantExists(tenantId);
        
        List<TenantConfiguration> configurations = configurationRepository.findAllByTenantId(tenantId);
        return configurations.stream()
                .collect(Collectors.toMap(
                        TenantConfiguration::getConfigKey,
                        TenantConfiguration::getConfigValue,
                        (oldValue, newValue) -> newValue,
                        HashMap::new
                ));
    }

    @Override
    @Transactional
    public TenantConfiguration createConfiguration(String tenantId, String key, String value) {
        log.info("Creating configuration for tenant {} with key {}", tenantId, key);
        validateTenantExists(tenantId);
        
        // Check if configuration already exists
        if (configurationRepository.existsByTenantIdAndKey(tenantId, key)) {
            return updateConfiguration(tenantId, key, value);
        }
        
        TenantConfiguration configuration = new TenantConfiguration(tenantId, key, value);
        return configurationRepository.save(configuration);
    }

    @Override
    @Transactional
    public TenantConfiguration createConfiguration(TenantConfiguration configuration) {
        log.info("Creating configuration for tenant {} with key {}", 
                configuration.getTenantId(), configuration.getConfigKey());
        validateTenantExists(configuration.getTenantId());
        
        // Check if configuration already exists
        if (configurationRepository.existsByTenantIdAndKey(
                configuration.getTenantId(), configuration.getConfigKey())) {
            TenantConfiguration existing = configurationRepository
                    .findByTenantIdAndKey(configuration.getTenantId(), configuration.getConfigKey())
                    .orElseThrow(() -> new ResourceNotFoundException("Configuration", 
                            configuration.getTenantId() + ":" + configuration.getConfigKey()));
            
            existing.setConfigValue(configuration.getConfigValue());
            existing.setDataType(configuration.getDataType());
            existing.setDescription(configuration.getDescription());
            existing.setEncrypted(configuration.isEncrypted());
            existing.setCategory(configuration.getCategory());
            existing.setActive(configuration.isActive());
            
            return configurationRepository.save(existing);
        }
        
        return configurationRepository.save(configuration);
    }

    @Override
    @Transactional
    public TenantConfiguration updateConfiguration(String tenantId, String key, String value) {
        log.info("Updating configuration for tenant {} with key {}", tenantId, key);
        validateTenantExists(tenantId);
        
        TenantConfiguration configuration = configurationRepository
                .findByTenantIdAndKey(tenantId, key)
                .orElseThrow(() -> new ResourceNotFoundException("Configuration", tenantId + ":" + key));
        
        configuration.setConfigValue(value);
        return configurationRepository.save(configuration);
    }

    @Override
    @Transactional
    public void deleteConfiguration(String tenantId, String key) {
        log.info("Deleting configuration for tenant {} with key {}", tenantId, key);
        validateTenantExists(tenantId);
        
        TenantConfiguration configuration = configurationRepository
                .findByTenantIdAndKey(tenantId, key)
                .orElseThrow(() -> new ResourceNotFoundException("Configuration", tenantId + ":" + key));
        
        // Soft delete by setting active to false
        configuration.setActive(false);
        configurationRepository.save(configuration);
    }

    @Override
    @Transactional(readOnly = true)
    public boolean hasConfiguration(String tenantId, String key) {
        log.debug("Checking if configuration exists for tenant {} with key {}", tenantId, key);
        return configurationRepository.existsByTenantIdAndKey(tenantId, key);
    }
    
    /**
     * Validate that a tenant exists
     * @param tenantId the tenant ID
     * @throws ResourceNotFoundException if tenant does not exist
     */
    private void validateTenantExists(String tenantId) {
        if (!tenantRepository.existsById(tenantId)) {
            throw new ResourceNotFoundException("Tenant", tenantId);
        }
    }
} 