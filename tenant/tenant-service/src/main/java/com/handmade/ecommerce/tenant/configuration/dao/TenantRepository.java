package com.handmade.ecommerce.tenant.configuration.dao;

import com.handmade.ecommerce.tenant.model.Tenant;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Repository for Tenant entity
 */
@Repository
public interface TenantRepository extends JpaRepository<Tenant, String> {

    /**
     * Find a tenant by name
     * @param tenantName the tenant name
     * @return the tenant if found
     */
    Optional<Tenant> findByTenantName(String tenantName);

    /**
     * Find tenants by status
     * @param status the status
     * @return list of tenants with the status
     */
    @Query("SELECT t FROM Tenant t WHERE t.state.name = :status")
    List<Tenant> findByStatus(@Param("status") String status);

    /**
     * Find active tenants
     * @return list of active tenants
     */
    @Query("SELECT t FROM Tenant t WHERE t.state.name = 'ACTIVE'")
    List<Tenant> findActiveTenants();

    /**
     * Check if a tenant is active
     * @param tenantId the tenant ID
     * @return true if active
     */
    @Query("SELECT COUNT(t) > 0 FROM Tenant t WHERE t.id = :tenantId AND t.state.name = 'ACTIVE'")
    boolean isActive(@Param("tenantId") String tenantId);
}
