package com.example.ambutracker.service;

import java.util.Map;

public interface GeocodingService {
    /**
     * Convert coordinates to a human-readable address
     * @param latitude The latitude coordinate
     * @param longitude The longitude coordinate
     * @return A map containing address information
     */
    Map<String, Object> reverseGeocode(double latitude, double longitude);
    
    /**
     * Convert an address to coordinates
     * @param address The address to geocode
     * @return A map containing latitude and longitude
     */
    Map<String, Object> geocode(String address);
} 