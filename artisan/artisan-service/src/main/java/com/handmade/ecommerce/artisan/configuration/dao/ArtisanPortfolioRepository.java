package com.handmade.ecommerce.artisan.configuration.dao;

import com.handmade.ecommerce.artisan.model.ArtisanPortfolio;
import com.handmade.ecommerce.artisan.model.ArtisanPortfolio.PortfolioStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ArtisanPortfolioRepository extends JpaRepository<ArtisanPortfolio, Long> {

} 