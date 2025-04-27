package com.handmade.ecommerce.price.service.impl;

import com.handmade.ecommerce.price.model.Price;
import com.handmade.ecommerce.price.repository.PriceRepository;
import com.handmade.ecommerce.price.client.ProductClient;
import com.handmade.ecommerce.price.service.PriceService;
import com.handmade.ecommerce.price.service.store.PriceEntityCoreStore;
import com.handmade.ecommerce.price.service.store.PriceEntityStore;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.math.BigDecimal;
import java.util.Currency;

@Service
public class PriceServiceImpl implements PriceService {
    private static final Logger logger = LoggerFactory.getLogger(PriceServiceImpl.class);

    private final PriceEntityCoreStore priceEntityCoreStore;

    public PriceServiceImpl(PriceEntityCoreStore priceEntityCoreStore) {
        this.priceEntityCoreStore = priceEntityCoreStore;
    }


    @Override
    public List<Price> getPricesByProductId(String productId) {
        validateProductExists(productId);
        return priceEntityCoreStore.findByProductIdOrderByValidFromDesc(productId);
    }

    @Override
    public Optional<Price> getCurrentPrice(String productId) {
        validateProductExists(productId);
        return priceEntityCoreStore.findTopByProductIdAndIsActiveTrueOrderByValidFromDesc(productId);
    }

    @Override
    @Transactional
    public Price createPrice(Price price) {
        validateProductExists(price.getProductId());
        validatePrice(price);
        
        // Set creation timestamp
        price.setCreatedAt(LocalDateTime.now());
        price.setUpdatedAt(LocalDateTime.now());
        
        // If this is the first price for the product, set it as active
        // If there's already an active price, set the new one as inactive
        price.setActive(priceEntityCoreStore.findByProductId(price.getProductId()).isEmpty());
        
         priceEntityCoreStore.store(price);
         return price;
    }

    @Override
    @Transactional
    public Optional<Price> updatePrice(String id, Price price) {
        validateProductExists(price.getProductId());
        validatePrice(price);
        
        return Optional.ofNullable(priceEntityCoreStore.findById(id)
                .map(existingPrice -> {
                    existingPrice.setAmount(price.getAmount());
                    existingPrice.setCurrency(price.getCurrency());
                    existingPrice.setValidFrom(price.getValidFrom());
                    existingPrice.setValidTo(price.getValidTo());
                    existingPrice.setActive(price.isActive());
                    existingPrice.setUpdatedAt(LocalDateTime.now());
                     priceEntityCoreStore.store(existingPrice);
                     return existingPrice;
                })
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Price not found")));
    }

    @Override
    @Transactional
    public Optional<Price> activatePrice(String id) {
        return Optional.ofNullable(priceEntityCoreStore.findById(id)
                .map(price -> {
                    validateProductExists(price.getProductId());

                    // Deactivate all prices for this product
                    List<Price> productPrices = priceEntityCoreStore.findByProductId(price.getProductId());
                    productPrices.forEach(p -> {
                        p.setActive(false);
                        p.setUpdatedAt(LocalDateTime.now());
                        priceEntityCoreStore.store(p);
                    });

                    // Activate the selected price
                    price.setActive(true);
                    price.setUpdatedAt(LocalDateTime.now());
                    priceEntityCoreStore.store(price);
                    return price;
                })
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Price not found")));
    }

    @Override
    @Transactional
    public void deletePrice(String id) {
        if (!priceEntityCoreStore.existsById(id)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Price not found");
        }
        priceEntityCoreStore.deleteById(id);
    }

    /**
     * Validate that a product exists
     * @param productId the ID of the product to validate
     * @throws ResponseStatusException if the product doesn't exist
     */
    private void validateProductExists(String productId) {
        if (!productClient.existsById(productId)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Product not found");
        }
    }

    /**
     * Validate a price
     * @param price the price to validate
     * @throws ResponseStatusException if the price is invalid
     */
    private void validatePrice(Price price) {
        // Validate amount is positive
        if (price.getAmount().compareTo(BigDecimal.ZERO) <= 0) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Price amount must be positive");
        }

        // Validate currency
        try {
            Currency.getInstance(price.getCurrency());
        } catch (IllegalArgumentException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid currency code");
        }

        // Validate validity period
        if (price.getValidFrom() == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Valid from date is required");
        }

        if (price.getValidTo() != null && price.getValidTo().isBefore(price.getValidFrom())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Valid to date must be after valid from date");
        }

        // Check for overlapping validity periods
        List<Price> existingPrices = priceRepository.findByProductId(price.getProductId());
        for (Price existingPrice : existingPrices) {
            if (existingPrice.getId().equals(price.getId())) {
                continue; // Skip the current price when updating
            }
            
            if (isOverlapping(price.getValidFrom(), price.getValidTo(), 
                            existingPrice.getValidFrom(), existingPrice.getValidTo())) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, 
                    "Price validity period overlaps with existing price");
            }
        }
    }

    /**
     * Check if two time periods overlap
     */
    private boolean isOverlapping(LocalDateTime start1, LocalDateTime end1, 
                                LocalDateTime start2, LocalDateTime end2) {
        if (end1 == null) end1 = LocalDateTime.MAX;
        if (end2 == null) end2 = LocalDateTime.MAX;
        
        return !start1.isAfter(end2) && !end1.isBefore(start2);
    }
}