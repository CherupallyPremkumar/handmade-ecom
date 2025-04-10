package com.handmade.ecommerce.tenant.service.store;

import org.chenile.utils.entity.service.EntityStore;
import com.handmade.ecommerce.tenant.model.Tenant;
import org.springframework.beans.factory.annotation.Autowired;
import org.chenile.base.exception.NotFoundException;
import com.handmade.ecommerce.tenant.configuration.dao.TenantRepository;
import java.util.Optional;

public class TenantEntityStore implements EntityStore<Tenant>{
    @Autowired private TenantRepository tenantRepository;

	@Override
	public void store(Tenant entity) {
        tenantRepository.save(entity);
	}

	@Override
	public Tenant retrieve(String id) {
        Optional<Tenant> entity = tenantRepository.findById(id);
        if (entity.isPresent()) return entity.get();
        throw new NotFoundException(1500,"Unable to find Tenant with ID " + id);
	}

}
