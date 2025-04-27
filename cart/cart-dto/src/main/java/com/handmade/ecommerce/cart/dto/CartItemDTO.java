package com.handmade.ecommerce.cart.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

/**
 * Data Transfer Object for CartItem
 */
@Data
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
@SuperBuilder
@Schema(description = "Cart item information")
public class CartItemDTO extends BaseDTO {
    
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
    
    @NotNull
    @Positive
    @Schema(description = "Total price for this item (quantity * unitPrice)", example = "59.98", required = true)
    private Double totalPrice;
    
    @Schema(description = "Cart identifier this item belongs to", example = "cart-123")
    private String cartId;
} 