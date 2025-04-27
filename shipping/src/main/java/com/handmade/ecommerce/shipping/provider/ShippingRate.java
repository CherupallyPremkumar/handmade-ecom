package com.handmade.ecommerce.shipping.provider;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * Represents a shipping rate offered by a shipping provider
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ShippingRate {
    
    /**
     * The shipping provider offering this rate
     */
    private String provider;
    
    /**
     * The service level (e.g., "Ground", "Express", "Next Day Air")
     */
    private String serviceLevel;
    
    /**
     * The cost of shipping
     */
    private BigDecimal cost;
    
    /**
     * The currency of the cost
     */
    private String currency;
    
    /**
     * The estimated delivery date
     */
    private LocalDateTime estimatedDeliveryDate;
    
    /**
     * The minimum delivery time in days
     */
    private Integer minDeliveryDays;
    
    /**
     * The maximum delivery time in days
     */
    private Integer maxDeliveryDays;
    
    /**
     * Whether this rate is guaranteed
     */
    private boolean guaranteed;
    
    /**
     * Additional features or benefits of this rate
     */
    private String features;
} 