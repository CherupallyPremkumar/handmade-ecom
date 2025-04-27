package com.handmade.ecommerce.artisan.dto.payload;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import java.util.Set;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Payload for verifying an artisan")
public class VerifyArtisanPayload {

    @NotBlank(message = "Verifier ID is required")
    @Schema(description = "ID of the user performing the verification")
    private String verifiedBy;

    @NotEmpty(message = "At least one verification document is required")
    @Schema(description = "Set of verification document references")
    private Set<String> documents;

    @Schema(description = "Additional notes about the verification")
    private String notes;

    @Schema(description = "Whether background check was passed")
    private boolean backgroundCheckPassed;

    @Schema(description = "Whether skills have been verified")
    private boolean skillsVerified;
} 