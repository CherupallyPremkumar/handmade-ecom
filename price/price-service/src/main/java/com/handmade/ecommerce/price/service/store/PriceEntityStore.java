package com.handmade.ecommerce.price.service.store;

import com.handmade.ecommerce.price.configuration.dao.PriceRepository;
import org.chenile.utils.entity.service.EntityStore;
import org.springframework.stereotype.Component;
import com.handmade.ecommerce.price.model.Price;
import java.util.List;
import java.util.Optional;
import java.util.function.Supplier;

@Component
public class PriceEntityStore implements EntityStore<Price> {
    
    private final PriceRepository priceRepository;
    
    public PriceEntityStore(PriceRepository priceRepository) {
        this.priceRepository = priceRepository;
    }
    
    
    public Optional<Price> findById(String id) {
        return priceRepository.findById(id);
    }
    
    public void deleteById(String id) {
        priceRepository.deleteById(id);
    }
    
    public List<Price> findByProductId(String productId) {
        return priceRepository.findByProductId(productId);
    }
    
    public List<Price> findByProductIdAndIsActiveTrue(String productId) {
        return priceRepository.findByProductIdAndIsActiveTrue(productId);
    }
    
    public List<Price> findAll() {
        return priceRepository.findAll();
    }

    @Override
    public void store(Price entity) {
        entity= priceRepository.save(entity);
    }

    @Override
    public Price retrieve(String id) {
        try {
            return priceRepository.findById(id).orElseThrow(new Supplier<Throwable>() {
                @Override
                public Throwable get() {
                    return new Throwable();
                }
            });
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }


    public boolean existsById(String id) {
    }
} 