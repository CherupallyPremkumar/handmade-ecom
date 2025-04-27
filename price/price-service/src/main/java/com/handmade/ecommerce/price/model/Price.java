package com.handmade.ecommerce.price.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.Column;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * Entity representing a price for a product.
 * A product can have multiple prices with different validity periods.
 * Only one price can be active at a time for a product.
 */
@Entity
@Table(name = "prices")
public class Price {
    
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;
    
    @NotNull
    @Column(name = "product_id", nullable = false)
    private String productId;

    /**
     * The price amount
     */
    @Column(name = "amount", nullable = false, precision = 19, scale = 4)
    private BigDecimal amount;

    /**
     * The currency code (ISO 4217)
     */
    @Column(name = "currency", nullable = false, length = 3)
    private String currency;

    /**
     * Whether this price is currently active
     */
    @Column(name = "is_active", nullable = false)
    private boolean isActive;

    /**
     * The start date of price validity
     */
    @Column(name = "valid_from", nullable = false)
    private LocalDateTime validFrom;

    /**
     * The end date of price validity (null means indefinite)
     */
    @Column(name = "valid_to")
    private LocalDateTime validTo;
} 