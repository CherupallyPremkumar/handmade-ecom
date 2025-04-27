package com.handmade.ecommerce.artisan.dto.payload;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import jakarta.validation.constraints.NotBlank;
import java.time.Duration;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Payload for suspending an artisan")
public class SuspendArtisanPayload {

    @NotBlank(message = "Suspender ID is required")
    @Schema(description = "ID of the user performing the suspension")
    private String suspendedBy;

    @NotBlank(message = "Suspension reason is required")
    @Schema(description = "Reason for the suspension")
    private String reason;

    @Schema(description = "Duration of the suspension period")
    private Duration duration;

    @Schema(description = "Additional notes about the suspension")
    private String notes;

    @Schema(description = "Whether immediate suspension is required")
    private boolean immediate;

    @Schema(description = "Whether the artisan can appeal the suspension")
    private boolean appealable;
} 