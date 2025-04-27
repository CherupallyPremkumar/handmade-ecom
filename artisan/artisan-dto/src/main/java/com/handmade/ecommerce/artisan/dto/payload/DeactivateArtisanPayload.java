package com.handmade.ecommerce.artisan.dto.payload;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import jakarta.validation.constraints.NotBlank;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Payload for deactivating an artisan")
public class DeactivateArtisanPayload {

    @NotBlank(message = "Deactivator ID is required")
    @Schema(description = "ID of the user performing the deactivation")
    private String deactivatedBy;

    @NotBlank(message = "Deactivation reason is required")
    @Schema(description = "Reason for the deactivation")
    private String reason;

    @Schema(description = "Additional notes about the deactivation")
    private String notes;

    @Schema(description = "Whether to archive the artisan's portfolio")
    private boolean archivePortfolio;

    @Schema(description = "Whether to notify affected customers")
    private boolean notifyCustomers;

    @Schema(description = "Whether the artisan can reapply in the future")
    private boolean canReapply;
} 