package com.handmade.ecommerce.artisan.model.service;

import com.handmade.ecommerce.artisan.model.Artisan;

import java.util.List;
import java.util.Optional;

public interface ArtisanService {
    
    /**
     * Find artisan by email
     * @param email the email to search for
     * @return Optional containing the artisan if found
     */
    Optional<Artisan> findByEmail(String email);
    
    /**
     * Find artisans by status
     * @param status the status to filter by
     * @return List of artisans with the given status
     */
    List<Artisan> findByStatus(String status);

} 