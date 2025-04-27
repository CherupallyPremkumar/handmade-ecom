package com.handmade.ecommerce.cart.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.util.List;

/**
 * Data Transfer Object for Cart
 */
@Data
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
@SuperBuilder
@Schema(description = "Shopping cart information")
public class CartDTO extends BaseDTO {
    
    @NotBlank
    @Schema(description = "User identifier", example = "user-123", required = true)
    private String userId;
    
    @Valid
    @Schema(description = "List of items in the cart")
    private List<CartItemDTO> items;
    
    @NotNull
    @Positive
    @Schema(description = "Total amount of the cart", example = "129.97", required = true)
    private Double totalAmount;
    
    @NotBlank
    @Schema(description = "Cart status (ACTIVE, CHECKED_OUT, ABANDONED)", example = "ACTIVE", required = true)
    private String status;
} 