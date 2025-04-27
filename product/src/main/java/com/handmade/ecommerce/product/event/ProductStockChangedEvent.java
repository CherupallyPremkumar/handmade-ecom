package com.handmade.ecommerce.product.event;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Data
@EqualsAndHashCode(callSuper = true)
@SuperBuilder
@NoArgsConstructor
public class ProductStockChangedEvent extends ProductEvent {
    private Integer oldQuantity;
    private Integer newQuantity;
    private String reason;
    private String warehouseId;
    private Boolean isLowStock;
    private Integer reorderPoint;
} 