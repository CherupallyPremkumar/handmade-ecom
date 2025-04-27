package com.handmade.ecommerce.artisan.service.impl;

import com.handmade.ecommerce.artisan.configuration.dao.ArtisanRepository;
import com.handmade.ecommerce.artisan.model.Artisan;
import com.handmade.ecommerce.artisan.service.ArtisanService;

import jakarta.annotation.PostConstruct;
import org.chenile.stm.STM;
import org.chenile.stm.State;
import org.chenile.stm.impl.STMActionsInfoProvider;
import org.chenile.utils.entity.service.EntityStore;
import org.chenile.workflow.service.impl.StateEntityServiceImpl;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class ArtisanServiceImpl extends StateEntityServiceImpl<Artisan> implements ArtisanService {

    private final   ArtisanRepository artisanRepository;
    
    // Cache for artisan status
    private final Map<String, State> artisanStatusCache = new ConcurrentHashMap<>();
    
    // Graph for artisan status transitions
    private final Map<String, Set<String>> statusTransitionGraph = new ConcurrentHashMap<>();

    public ArtisanServiceImpl(STM<Artisan> artisanEntityStm, STMActionsInfoProvider artisanActionsInfoProvider, EntityStore<Artisan> artisanEntityStore, ArtisanRepository artisanRepository) {
        super(artisanEntityStm, artisanActionsInfoProvider, artisanEntityStore);
        this.artisanRepository = artisanRepository;
    }

    @PostConstruct
    public void initialize() {
        // Load all artisans and build the status cache
        artisanRepository.findAll().forEach(artisan -> 
            artisanStatusCache.put(artisan.getId(), artisan.getCurrentState()));
    }

    @Override
    @Transactional(readOnly = true)
    @Cacheable(value = "artisanByEmail", key = "#email")
    public Optional<Artisan> findByEmail(String email) {
        return artisanRepository.findByEmail(email);
    }

    @Override
    @Transactional(readOnly = true)
    @Cacheable(value = "artisansByStatus", key = "#status")
    public List<Artisan> findByStatus(String status) {
        return artisanRepository.findByStateId(status);
    }
} 