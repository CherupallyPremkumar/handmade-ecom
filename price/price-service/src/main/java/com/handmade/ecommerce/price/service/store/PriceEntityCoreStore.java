package com.handmade.ecommerce.price.service.store;

import com.handmade.ecommerce.price.configuration.dao.PriceRepository;
import com.handmade.ecommerce.price.model.Price;

import java.util.List;
import java.util.Optional;

public class PriceEntityCoreStore extends PriceEntityStore{
    
    public PriceEntityCoreStore(PriceRepository priceRepository) {
        super(priceRepository);
    }

    public List<Price> findByProductIdOrderByValidFromDesc(String productId) {
    }

    public Optional<Price> findTopByProductIdAndIsActiveTrueOrderByValidFromDesc(String productId) {
    }
}
