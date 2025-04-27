package com.handmade.ecommerce.artisan.dto.payload;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import jakarta.validation.constraints.NotBlank;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Payload for activating an artisan")
public class ActivateArtisanPayload {

    @NotBlank(message = "Activator ID is required")
    @Schema(description = "ID of the user performing the activation")
    private String activatedBy;

    @Schema(description = "Additional notes about the activation")
    private String notes;

    @Schema(description = "Whether training has been completed")
    private boolean trainingCompleted;

    @Schema(description = "Whether all required documents are submitted")
    private boolean documentsSubmitted;

    @Schema(description = "Whether quality standards are met")
    private boolean qualityStandardsMet;
} 