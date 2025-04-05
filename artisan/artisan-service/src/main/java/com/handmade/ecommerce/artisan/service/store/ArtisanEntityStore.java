package com.handmade.ecommerce.artisan.service.store;

import org.chenile.utils.entity.service.EntityStore;
import com.handmade.ecommerce.artisan.model.Artisan;
import org.springframework.beans.factory.annotation.Autowired;
import org.chenile.base.exception.NotFoundException;
import com.handmade.ecommerce.artisan.configuration.dao.ArtisanRepository;
import java.util.Optional;

public class ArtisanEntityStore implements EntityStore<Artisan>{
    @Autowired private ArtisanRepository artisanRepository;

	@Override
	public void store(Artisan entity) {
        artisanRepository.save(entity);
	}

	@Override
	public Artisan retrieve(String id) {
        Optional<Artisan> entity = artisanRepository.findById(id);
        if (entity.isPresent()) return entity.get();
        throw new NotFoundException(1500,"Unable to find Artisan with ID " + id);
	}

}
