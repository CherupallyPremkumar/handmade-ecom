package com.handmade.ecommerce.price.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.time.LocalDateTime;
import java.util.Date;

/**
 * Base DTO class with common fields for all DTOs
 */
@Data
@NoArgsConstructor
@SuperBuilder
public abstract class BaseDTO {
    
    @Schema(description = "Unique identifier", example = "123e4567-e89b-12d3-a456-426614174000")
    private String id;

    private String tenantId;
    
    @Schema(description = "Creation timestamp")
    private Date createdAt;

    @Schema(description = "Last update timestamp")
    private Date updatedAt;
} 