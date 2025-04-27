package com.handmade.ecommerce.product.event;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@EqualsAndHashCode(callSuper = true)
@SuperBuilder
@NoArgsConstructor
public class ProductPriceChangedEvent extends ProductEvent {
    private BigDecimal oldPrice;
    private BigDecimal newPrice;
    private String currency;
    private String reason;
    private Boolean isPromotion;
    private LocalDateTime effectiveFrom;
    private LocalDateTime effectiveTo;
} 