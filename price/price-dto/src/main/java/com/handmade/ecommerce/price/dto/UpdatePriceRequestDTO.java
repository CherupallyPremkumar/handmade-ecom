package com.handmade.ecommerce.price.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * Request DTO for updating an existing price
 */
@Data
@NoArgsConstructor
@Schema(description = "Request to update an existing price")
public class UpdatePriceRequestDTO  extends BaseDTO{
    
    @NotNull
    @Schema(description = "Product identifier", example = "product-123", required = true)
    private String productId;
    
    @NotNull
    @Positive
    @Schema(description = "Price amount", example = "99.99", required = true)
    private BigDecimal amount;
    
    @NotNull
    @Size(min = 3, max = 3)
    @Schema(description = "Currency code (ISO 4217)", example = "USD", required = true)
    private String currency;
    
    @NotNull
    @Schema(description = "Start date of price validity", example = "2023-01-01T00:00:00", required = true)
    private LocalDateTime validFrom;
    
    @Schema(description = "End date of price validity (null means indefinite)", example = "2023-12-31T23:59:59")
    private LocalDateTime validTo;
    
    @Schema(description = "Whether this price should be active", example = "true")
    private Boolean isActive;
} 