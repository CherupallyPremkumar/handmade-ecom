package com.handmade.ecommerce.tenant.configuration.dao;

import com.handmade.ecommerce.tenant.model.Tenant;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository  public interface TenantRepository extends JpaRepository<Tenant,String> {}
