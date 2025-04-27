package com.handmade.ecommerce.cart.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Request DTO for updating a cart item
 */
@Data
@NoArgsConstructor
@Schema(description = "Request to update a cart item")
public class UpdateItemRequestDTO  extends BaseDTO{
    
    @NotNull
    @Positive
    @Schema(description = "New quantity of the product", example = "3", required = true)
    private Integer quantity;
} 