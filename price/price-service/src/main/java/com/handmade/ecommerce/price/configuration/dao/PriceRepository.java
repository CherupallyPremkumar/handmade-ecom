package com.handmade.ecommerce.price.configuration.dao;

import com.handmade.ecommerce.price.model.Price;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository  public interface PriceRepository extends JpaRepository<Price,String> {


    /**
     * Find all prices for a specific product
     * @param productId the ID of the product
     * @return a list of prices for the product
     */
    List<Price> findByProductId(String productId);

    /**
     * Find all active prices for a specific product
     * @param productId the ID of the product
     * @return a list of active prices for the product
     */
    List<Price> findByProductIdAndIsActiveTrue(String productId);

    /**
     * Find the most recent active price for a specific product
     * @param productId the ID of the product
     * @return the most recent active price for the product
     */
    Optional<Price> findTopByProductIdAndIsActiveTrueOrderByValidFromDesc(String productId);

    /**
     * Find all prices for a specific product ordered by valid from date descending
     * @param productId the ID of the product
     * @return a list of prices for the product ordered by valid from date descending
     */
    List<Price> findByProductIdOrderByValidFromDesc(String productId);
}
