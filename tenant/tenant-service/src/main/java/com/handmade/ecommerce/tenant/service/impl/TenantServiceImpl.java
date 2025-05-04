package com.handmade.ecommerce.tenant.service.impl;

import com.handmade.ecommerce.common.exception.ResourceNotFoundException;
import com.handmade.ecommerce.common.exception.ValidationException;
import com.handmade.ecommerce.tenant.configuration.dao.TenantRepository;
import com.handmade.ecommerce.tenant.model.Tenant;
import com.handmade.ecommerce.tenant.model.service.TenantService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.chenile.workflow.service.StateEntityService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

/**
 * Implementation of TenantService.
 * Provides methods to manage tenants.
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class TenantServiceImpl implements TenantService {

    private final TenantRepository tenantRepository;
    private final StateEntityService<Tenant> stateEntityService;

    @Override
    @Transactional
    public Tenant createTenant(Tenant tenant) {
        log.info("Creating tenant with name: {}", tenant.getTenantName());
        
        // Check if tenant with same name already exists
        if (tenantRepository.findByTenantName(tenant.getTenantName()).isPresent()) {
            throw new ValidationException("tenantName", tenant.getTenantName(), 
                    "A tenant with this name already exists");
        }
        
        // Create tenant with initial OPENED state
        Tenant savedTenant = stateEntityService.createAndProcess(tenant);
        log.info("Created tenant with ID: {}", savedTenant.getId());
        return savedTenant;
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Tenant> getTenantById(String tenantId) {
        log.debug("Getting tenant with ID: {}", tenantId);
        return tenantRepository.findById(tenantId);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Tenant> getAllTenants() {
        log.debug("Getting all tenants");
        return tenantRepository.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public List<Tenant> getTenantsByStatus(String status) {
        log.debug("Getting tenants with status: {}", status);
        return tenantRepository.findByStatus(status);
    }

    @Override
    @Transactional
    public Tenant assignTenant(String tenantId) {
        log.info("Assigning tenant with ID: {}", tenantId);
        Tenant tenant = getTenantOrThrow(tenantId);
        return (Tenant) stateEntityService.processEvent(tenant, "assign", null);
    }

    @Override
    @Transactional
    public Tenant approveTenant(String tenantId) {
        log.info("Approving tenant with ID: {}", tenantId);
        Tenant tenant = getTenantOrThrow(tenantId);
        return (Tenant) stateEntityService.processEvent(tenant, "approve", null);
    }

    @Override
    @Transactional
    public Tenant activateTenant(String tenantId) {
        log.info("Activating tenant with ID: {}", tenantId);
        Tenant tenant = getTenantOrThrow(tenantId);
        return (Tenant) stateEntityService.processEvent(tenant, "activate", null);
    }

    @Override
    @Transactional
    public Tenant suspendTenant(String tenantId, String reason) {
        log.info("Suspending tenant with ID: {}, reason: {}", tenantId, reason);
        Tenant tenant = getTenantOrThrow(tenantId);
        // You could use a payload object here to carry the reason
        return (Tenant) stateEntityService.processEvent(tenant, "suspend", reason);
    }

    @Override
    @Transactional
    public Tenant reactivateTenant(String tenantId) {
        log.info("Reactivating tenant with ID: {}", tenantId);
        Tenant tenant = getTenantOrThrow(tenantId);
        return (Tenant) stateEntityService.processEvent(tenant, "reactivate", null);
    }

    @Override
    @Transactional
    public Tenant closeTenant(String tenantId, String reason) {
        log.info("Closing tenant with ID: {}, reason: {}", tenantId, reason);
        Tenant tenant = getTenantOrThrow(tenantId);
        // You could use a payload object here to carry the reason
        return (Tenant) stateEntityService.processEvent(tenant, "close", reason);
    }

    @Override
    @Transactional(readOnly = true)
    public boolean isTenantActive(String tenantId) {
        log.debug("Checking if tenant is active: {}", tenantId);
        return tenantRepository.isActive(tenantId);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Tenant> getTenantByName(String tenantName) {
        log.debug("Getting tenant by name: {}", tenantName);
        return tenantRepository.findByTenantName(tenantName);
    }

    /**
     * Get a tenant by ID or throw an exception if not found
     * @param tenantId the tenant ID
     * @return the tenant
     * @throws ResourceNotFoundException if tenant not found
     */
    private Tenant getTenantOrThrow(String tenantId) {
        return tenantRepository.findById(tenantId)
                .orElseThrow(() -> new ResourceNotFoundException("Tenant", tenantId));
    }
} 