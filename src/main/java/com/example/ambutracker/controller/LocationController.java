package com.example.ambutracker.controller;

import com.example.ambutracker.dto.LocationDTO;
import com.example.ambutracker.service.GeocodingService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/location")
@RequiredArgsConstructor
@Tag(name = "Location", description = "Location management APIs")
public class LocationController {

    private final GeocodingService geocodingService;

    @GetMapping("/current")
    @Operation(
        summary = "Get current location instructions",
        description = "Returns instructions for getting the current location from the device"
    )
    public ResponseEntity<LocationDTO> getCurrentLocationInstructions() {
        // Return instructions for getting location from browser
        LocationDTO instructions = LocationDTO.builder()
            .latitude(0.0)
            .longitude(0.0)
            .address("Use browser's Geolocation API to get current location")
            .build();
        return ResponseEntity.ok(instructions);
    }

    @GetMapping("/reverse-geocode")
    @Operation(
        summary = "Reverse geocode coordinates",
        description = "Convert latitude and longitude to address"
    )
    public ResponseEntity<LocationDTO> reverseGeocode(
            @RequestParam double latitude,
            @RequestParam double longitude) {
        Map<String, Object> geocodeResult = geocodingService.reverseGeocode(latitude, longitude);
        String address = (String) geocodeResult.get("formattedAddress");
        LocationDTO location = LocationDTO.builder()
            .latitude(latitude)
            .longitude(longitude)
            .address(address)
            .build();
        return ResponseEntity.ok(location);
    }

    @GetMapping("/geocode")
    @Operation(
        summary = "Convert address to coordinates",
        description = "Converts a human-readable address to latitude and longitude coordinates"
    )
    public ResponseEntity<Map<String, Object>> geocode(
            @Parameter(description = "Address to geocode") @RequestParam String address) {
        return ResponseEntity.ok(geocodingService.geocode(address));
    }
} 