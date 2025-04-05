package com.handmade.ecommerce.return.service.store;

import org.chenile.utils.entity.service.EntityStore;
import com.handmade.ecommerce.return.model.Return;
import org.springframework.beans.factory.annotation.Autowired;
import org.chenile.base.exception.NotFoundException;
import com.handmade.ecommerce.return.configuration.dao.ReturnRepository;
import java.util.Optional;

public class ReturnEntityStore implements EntityStore<Return>{
    @Autowired private ReturnRepository returnRepository;

	@Override
	public void store(Return entity) {
        returnRepository.save(entity);
	}

	@Override
	public Return retrieve(String id) {
        Optional<Return> entity = returnRepository.findById(id);
        if (entity.isPresent()) return entity.get();
        throw new NotFoundException(1500,"Unable to find Return with ID " + id);
	}

}
