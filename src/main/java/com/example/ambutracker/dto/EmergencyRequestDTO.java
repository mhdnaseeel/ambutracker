package com.example.ambutracker.dto;

import com.example.ambutracker.entity.EmergencyType;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import lombok.Builder;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Emergency Request Data Transfer Object")
public class EmergencyRequestDTO {
    @Schema(description = "Type of emergency", example = "MEDICAL")
    @NotNull(message = "Emergency type is required")
    private EmergencyType emergencyType;

    @Schema(description = "Name of the patient", example = "John Doe")
    @NotBlank(message = "Patient name is required")
    private String patientName;

    @Schema(description = "Age of the patient", example = "45")
    @NotNull(message = "Patient age is required")
    private Integer patientAge;

    @Schema(description = "Condition of the patient", example = "Chest Pain")
    @NotNull(message = "Patient condition is required")
    private String patientCondition;

    @Schema(description = "Pickup location description", example = "123 Main St")
    @NotBlank(message = "Pickup location is required")
    private String pickupLocation;

    @Schema(description = "Pickup location latitude", example = "40.7128")
    @NotNull(message = "Pickup latitude is required")
    private Double pickupLatitude;

    @Schema(description = "Pickup location longitude", example = "-74.0060")
    @NotNull(message = "Pickup longitude is required")
    private Double pickupLongitude;

    @Schema(description = "Destination location description", example = "City Hospital")
    @NotBlank(message = "Destination location is required")
    private String destinationLocation;

    @Schema(description = "Destination latitude", example = "40.7589")
    @NotNull(message = "Destination latitude is required")
    private Double destinationLatitude;

    @Schema(description = "Destination longitude", example = "-73.9851")
    @NotNull(message = "Destination longitude is required")
    private Double destinationLongitude;

    @Schema(description = "Additional description of the emergency", example = "Patient experiencing severe chest pain")
    private String description;

    private Double latitude;
    private Double longitude;

    private Long requesterId;
} 