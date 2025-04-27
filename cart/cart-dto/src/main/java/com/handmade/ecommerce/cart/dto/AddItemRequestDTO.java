package com.handmade.ecommerce.cart.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Request DTO for adding an item to a cart
 */
@Data
@NoArgsConstructor
@Schema(description = "Request to add an item to a cart")
public class AddItemRequestDTO extends BaseDTO{
    
    @NotBlank
    @Schema(description = "Product identifier", example = "product-123", required = true)
    private String productId;
    
    @NotBlank
    @Schema(description = "Product name", example = "Premium Widget", required = true)
    private String productName;
    
    @NotNull
    @Positive
    @Schema(description = "Quantity of the product", example = "2", required = true)
    private Integer quantity;
    
    @NotNull
    @Positive
    @Schema(description = "Unit price of the product", example = "29.99", required = true)
    private Double unitPrice;
} 