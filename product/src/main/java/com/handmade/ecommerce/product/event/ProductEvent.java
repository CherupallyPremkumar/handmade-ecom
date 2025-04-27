package com.handmade.ecommerce.product.event;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.time.LocalDateTime;

@Data
@SuperBuilder
@NoArgsConstructor
public abstract class ProductEvent {
    private String eventId;
    private String productId;
    private String productName;
    private LocalDateTime timestamp;
    private String category;
} 