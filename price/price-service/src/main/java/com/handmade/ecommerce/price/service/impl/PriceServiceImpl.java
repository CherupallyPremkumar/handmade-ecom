package com.handmade.ecommerce.price.service.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


import com.handmade.ecommerce.price.model.Price;
import com.handmade.ecommerce.price.service.PriceService;

import com.handmade.ecommerce.price.configuration.dao.PriceRepository;
import org.chenile.base.exception.NotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import java.util.Optional;

public class PriceServiceImpl implements PriceService{
    private static final Logger logger = LoggerFactory.getLogger(PriceServiceImpl.class);
    @Autowired
    PriceRepository priceRepository;
    @Override
    public Price save(Price entity) {
        entity = priceRepository.save(entity);
        return entity;
    }

    @Override
    public Price retrieve(String id) {
        Optional<Price> entity = priceRepository.findById(id);
        if (entity.isPresent()) return entity.get();
        throw new NotFoundException(1500,"Unable to find price with ID " + id);
    }
}