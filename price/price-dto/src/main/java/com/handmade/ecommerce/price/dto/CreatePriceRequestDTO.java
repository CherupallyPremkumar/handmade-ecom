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
 * Request DTO for creating a new price
 */
@Data
@NoArgsConstructor
@Schema(description = "Request to create a new price")
public class CreatePriceRequestDTO extends BaseDTO{
    
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
    
    @Schema(description = "Start date of price validity (defaults to current time if not specified)", example = "2023-01-01T00:00:00")
    private LocalDateTime validFrom;
    
    @Schema(description = "End date of price validity (null means indefinite)", example = "2023-12-31T23:59:59")
    private LocalDateTime validTo;
} 