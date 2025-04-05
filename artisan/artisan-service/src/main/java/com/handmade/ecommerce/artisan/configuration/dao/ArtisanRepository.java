package com.handmade.ecommerce.artisan.configuration.dao;

import com.handmade.ecommerce.artisan.model.Artisan;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository  public interface ArtisanRepository extends JpaRepository<Artisan,String> {}
