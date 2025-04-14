package com.example.ambutracker.controller;

import com.example.ambutracker.dto.EmergencyRequestDTO;
import com.example.ambutracker.entity.Emergency;
import com.example.ambutracker.entity.EmergencyStatus;
import com.example.ambutracker.entity.User;
import com.example.ambutracker.service.EmergencyService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/emergencies")
@Tag(name = "Emergency Management", description = "APIs for managing emergency requests")
@SecurityRequirement(name = "bearerAuth")
public class EmergencyController {

    private final EmergencyService emergencyService;

    @Autowired
    public EmergencyController(EmergencyService emergencyService) {
        this.emergencyService = emergencyService;
    }

    @PostMapping
    @Operation(
        summary = "Create a new emergency request",
        description = "Creates a new emergency request with the provided details"
    )
    public ResponseEntity<Emergency> createEmergency(
            @Valid @RequestBody EmergencyRequestDTO requestDTO,
            @AuthenticationPrincipal User user) {
        Emergency emergency = emergencyService.createEmergencyRequest(requestDTO, user);
        return ResponseEntity.ok(emergency);
    }

    @GetMapping("/{id}")
    @Operation(
        summary = "Get emergency by ID",
        description = "Retrieves an emergency request by its ID"
    )
    public ResponseEntity<Emergency> getEmergencyById(
            @Parameter(description = "Emergency ID") @PathVariable Long id) {
        Emergency emergency = emergencyService.getEmergencyById(id);
        return ResponseEntity.ok(emergency);
    }

    @GetMapping
    @Operation(
        summary = "Get all emergencies",
        description = "Retrieves all emergency requests"
    )
    public ResponseEntity<List<Emergency>> getAllEmergencies() {
        List<Emergency> emergencies = emergencyService.getAllEmergencies();
        return ResponseEntity.ok(emergencies);
    }

    @GetMapping("/status/{status}")
    @Operation(
        summary = "Get emergencies by status",
        description = "Retrieves all emergency requests with the specified status"
    )
    public ResponseEntity<List<Emergency>> getEmergenciesByStatus(
            @Parameter(description = "Emergency status (PENDING, ASSIGNED, IN_PROGRESS, COMPLETED, CANCELLED)")
            @PathVariable EmergencyStatus status) {
        List<Emergency> emergencies = emergencyService.getEmergenciesByStatus(status);
        return ResponseEntity.ok(emergencies);
    }

    @PutMapping("/{id}/status")
    @Operation(
        summary = "Update emergency status",
        description = "Updates the status of an emergency request"
    )
    public ResponseEntity<Emergency> updateEmergencyStatus(
            @Parameter(description = "Emergency ID") @PathVariable Long id,
            @RequestBody EmergencyStatus status) {
        Emergency emergency = emergencyService.updateEmergencyStatus(id, status);
        return ResponseEntity.ok(emergency);
    }

    @PutMapping("/{id}/location")
    @Operation(
        summary = "Update emergency location",
        description = "Updates the location coordinates of an emergency request"
    )
    public ResponseEntity<Emergency> updateEmergencyLocation(
            @Parameter(description = "Emergency ID") @PathVariable Long id,
            @Parameter(description = "New latitude") @RequestParam Double latitude,
            @Parameter(description = "New longitude") @RequestParam Double longitude) {
        Emergency emergency = emergencyService.updateEmergencyLocation(id, latitude, longitude);
        return ResponseEntity.ok(emergency);
    }

    @PutMapping("/{id}/assign/{ambulanceId}")
    @Operation(
        summary = "Assign ambulance to emergency",
        description = "Assigns an ambulance to handle the emergency request"
    )
    public ResponseEntity<Emergency> assignAmbulance(
            @Parameter(description = "Emergency ID") @PathVariable Long id,
            @Parameter(description = "Ambulance ID") @PathVariable Long ambulanceId) {
        Emergency emergency = emergencyService.assignAmbulance(id, ambulanceId);
        return ResponseEntity.ok(emergency);
    }

    @DeleteMapping("/{id}")
    @Operation(
        summary = "Delete emergency",
        description = "Deletes an emergency request by its ID"
    )
    public ResponseEntity<Void> deleteEmergency(
            @Parameter(description = "Emergency ID") @PathVariable Long id) {
        emergencyService.deleteEmergency(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/nearby")
    @Operation(
        summary = "Find nearby emergencies",
        description = "Finds emergency requests within a specified radius of given coordinates"
    )
    public ResponseEntity<List<Emergency>> findNearbyEmergencies(
            @Parameter(description = "Latitude coordinate") @RequestParam Double latitude,
            @Parameter(description = "Longitude coordinate") @RequestParam Double longitude,
            @Parameter(description = "Search radius in kilometers") @RequestParam Double radiusInKm) {
        List<Emergency> emergencies = emergencyService.findNearbyEmergencies(latitude, longitude, radiusInKm);
        return ResponseEntity.ok(emergencies);
    }
} 