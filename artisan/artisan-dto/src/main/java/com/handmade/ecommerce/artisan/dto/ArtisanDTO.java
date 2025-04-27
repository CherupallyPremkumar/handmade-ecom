package com.handmade.ecommerce.artisan.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Size;
import java.time.LocalDateTime;
import java.util.Set;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Represents an artisan in the system")
public class ArtisanDTO {

    @Schema(description = "Unique identifier of the artisan")
    private String id;

    @NotBlank(message = "Name is required")
    @Size(min = 2, max = 100, message = "Name must be between 2 and 100 characters")
    @Schema(description = "Full name of the artisan")
    private String name;

    @NotBlank(message = "Email is required")
    @Email(message = "Invalid email format")
    @Schema(description = "Email address of the artisan")
    private String email;

    @Schema(description = "Contact phone number of the artisan")
    private String phone;

    @Schema(description = "Current status of the artisan (REGISTERED, VERIFIED, ACTIVE, SUSPENDED, INACTIVE)")
    private String status;

    @Schema(description = "Whether the artisan is currently active")
    private boolean active;

    @Schema(description = "Set of skills/specialties of the artisan")
    private Set<String> skills;

    @Schema(description = "Brief description of the artisan's work")
    private String description;

    @Schema(description = "Date when the artisan was registered")
    private LocalDateTime registrationDate;

    @Schema(description = "Date when the artisan was verified")
    private LocalDateTime verificationDate;

    @Schema(description = "User who verified the artisan")
    private String verifiedBy;

    @Schema(description = "Date when the artisan was activated")
    private LocalDateTime activationDate;

    @Schema(description = "User who activated the artisan")
    private String activatedBy;

    @Schema(description = "Date when the artisan was suspended, if applicable")
    private LocalDateTime suspensionDate;

    @Schema(description = "User who suspended the artisan")
    private String suspendedBy;

    @Schema(description = "Reason for suspension, if applicable")
    private String suspensionReason;

    @Schema(description = "Date when the artisan was deactivated, if applicable")
    private LocalDateTime deactivationDate;

    @Schema(description = "User who deactivated the artisan")
    private String deactivatedBy;

    @Schema(description = "Reason for deactivation, if applicable")
    private String deactivationReason;
} 