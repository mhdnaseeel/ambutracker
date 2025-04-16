package com.example.ambutracker.service.impl;

import com.example.ambutracker.service.GeocodingService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Map;

@Service
public class GoogleGeocodingServiceImpl implements GeocodingService {

    private final RestTemplate restTemplate;
    private final String apiKey;

    public GoogleGeocodingServiceImpl(
            @Value("${google.maps.api.key:AIzaSyCICKA-61BmahcBSZBx-w99vu-kMaZyepg}") String apiKey) {
        this.restTemplate = new RestTemplate();
        this.apiKey = apiKey;
    }

    @Override
    public Map<String, Object> reverseGeocode(double latitude, double longitude) {
        String url = String.format(
            "https://maps.googleapis.com/maps/api/geocode/json?latlng=%f,%f&key=%s",
            latitude, longitude, apiKey
        );
        
        Map<String, Object> response = restTemplate.getForObject(url, Map.class);
        Map<String, Object> result = new HashMap<>();
        
        if (response != null && "OK".equals(response.get("status"))) {
            Map<String, Object>[] results = (Map<String, Object>[]) response.get("results");
            if (results != null && results.length > 0) {
                result.put("formattedAddress", results[0].get("formatted_address"));
                result.put("latitude", latitude);
                result.put("longitude", longitude);
                return result;
            }
        }
        
        result.put("error", "Could not reverse geocode the coordinates");
        return result;
    }

    @Override
    public Map<String, Object> geocode(String address) {
        String url = String.format(
            "https://maps.googleapis.com/maps/api/geocode/json?address=%s&key=%s",
            address.replace(" ", "+"), apiKey
        );
        
        Map<String, Object> response = restTemplate.getForObject(url, Map.class);
        Map<String, Object> result = new HashMap<>();
        
        if (response != null && "OK".equals(response.get("status"))) {
            Map<String, Object>[] results = (Map<String, Object>[]) response.get("results");
            if (results != null && results.length > 0) {
                Map<String, Object> geometry = (Map<String, Object>) results[0].get("geometry");
                Map<String, Object> location = (Map<String, Object>) geometry.get("location");
                
                result.put("latitude", location.get("lat"));
                result.put("longitude", location.get("lng"));
                result.put("formattedAddress", results[0].get("formatted_address"));
                return result;
            }
        }
        
        result.put("error", "Could not geocode the address");
        return result;
    }
} 