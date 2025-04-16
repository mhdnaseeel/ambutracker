package com.example.ambutracker.service.impl;

import com.example.ambutracker.dto.EmergencyRequestDTO;
import com.example.ambutracker.entity.Emergency;
import com.example.ambutracker.entity.EmergencyStatus;
import com.example.ambutracker.entity.User;
import com.example.ambutracker.model.EmergencyRequest;
import com.example.ambutracker.repository.EmergencyRepository;
import com.example.ambutracker.service.EmergencyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;

import jakarta.persistence.EntityNotFoundException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;
import java.util.Map;

@Service
public class EmergencyServiceImpl implements EmergencyService {

    private final EmergencyRepository emergencyRepository;
    private final RestTemplate restTemplate;

    @Value("${google.maps.api.key}")
    private String googleMapsApiKey;

    @Autowired
    public EmergencyServiceImpl(EmergencyRepository emergencyRepository, RestTemplate restTemplate) {
        this.emergencyRepository = emergencyRepository;
        this.restTemplate = restTemplate;
    }

    @Override
    @Transactional
    public Emergency createEmergencyRequest(EmergencyRequestDTO requestDTO, User requester) {
        Emergency emergency = new Emergency();
        emergency.setRequester(requester);
        emergency.setEmergencyType(requestDTO.getEmergencyType());
        emergency.setPatientName(requestDTO.getPatientName());
        emergency.setPatientAge(requestDTO.getPatientAge());
        emergency.setPatientCondition(requestDTO.getPatientCondition());
        emergency.setPickupLocation(requestDTO.getPickupLocation());
        emergency.setPickupLatitude(requestDTO.getPickupLatitude());
        emergency.setPickupLongitude(requestDTO.getPickupLongitude());
        emergency.setDestinationLocation(requestDTO.getDestinationLocation());
        emergency.setDestinationLatitude(requestDTO.getDestinationLatitude());
        emergency.setDestinationLongitude(requestDTO.getDestinationLongitude());
        emergency.setDescription(requestDTO.getDescription());
        emergency.setLatitude(requestDTO.getPickupLatitude());
        emergency.setLongitude(requestDTO.getPickupLongitude());
        emergency.setStatus(EmergencyStatus.PENDING);
        emergency.setCreatedAt(LocalDateTime.now());
        emergency.setUpdatedAt(LocalDateTime.now());
        
        // TODO: Add validation logic
        // TODO: Add notification logic
        // TODO: Add hospital assignment logic
        
        return emergencyRepository.save(emergency);
    }

    @Override
    public Emergency getEmergencyById(Long id) {
        return emergencyRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Emergency not found with id: " + id));
    }

    @Override
    public List<Emergency> getAllEmergencies() {
        return emergencyRepository.findAll(Sort.by(Sort.Direction.DESC, "createdAt"));
    }

    @Override
    public List<Emergency> getEmergenciesByStatus(EmergencyStatus status) {
        return emergencyRepository.findByStatus(status);
    }

    @Override
    public List<Emergency> getEmergenciesByDispatcher(User dispatcher) {
        // Since we've simplified our model, we'll return emergencies based on status
        // In a real implementation, you might want to add dispatcher-specific logic
        return emergencyRepository.findByStatus(EmergencyStatus.PENDING);
    }

    @Override
    @Transactional
    public Emergency assignAmbulance(Long emergencyId, Long ambulanceId) {
        Emergency emergency = getEmergencyById(emergencyId);
        // TODO: Implement ambulance assignment logic
        emergency.setStatus(EmergencyStatus.ASSIGNED);
        return emergencyRepository.save(emergency);
    }

    @Override
    @Transactional
    public Emergency updateEmergencyStatus(Long id, EmergencyStatus status) {
        Emergency emergency = getEmergencyById(id);
        emergency.setStatus(status);
        return emergencyRepository.save(emergency);
    }

    @Override
    @Transactional
    public Emergency updateEmergencyLocation(Long id, Double latitude, Double longitude) {
        Emergency emergency = getEmergencyById(id);
        emergency.setLatitude(latitude);
        emergency.setLongitude(longitude);
        return emergencyRepository.save(emergency);
    }

    @Override
    @Transactional
    public void deleteEmergency(Long id) {
        Emergency emergency = getEmergencyById(id);
        emergencyRepository.delete(emergency);
    }

    @Override
    public List<Emergency> findNearbyEmergencies(Double latitude, Double longitude, Double radiusInKm) {
        // Get all emergencies and filter them based on distance
        // This is a simple implementation that gets the 10 most recent emergencies
        // In a production environment, you would want to use a spatial database query
        List<Emergency> allEmergencies = emergencyRepository.findAll(
            PageRequest.of(0, 10, Sort.by(Sort.Direction.DESC, "createdAt"))
        ).getContent();

        // Filter emergencies within the specified radius
        return allEmergencies.stream()
            .filter(emergency -> calculateDistance(
                latitude, longitude,
                emergency.getLatitude(), emergency.getLongitude()
            ) <= radiusInKm)
            .collect(Collectors.toList());
    }

    // Haversine formula to calculate distance between two points on Earth
    private double calculateDistance(double lat1, double lon1, double lat2, double lon2) {
        final int R = 6371; // Earth's radius in kilometers

        double latDistance = Math.toRadians(lat2 - lat1);
        double lonDistance = Math.toRadians(lon2 - lon1);

        double a = Math.sin(latDistance / 2) * Math.sin(latDistance / 2)
                + Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2))
                * Math.sin(lonDistance / 2) * Math.sin(lonDistance / 2);

        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));

        return R * c;
    }

    @Override
    @Transactional
    public void processEmergencyRequest(EmergencyRequest request) {
        // Set initial status and timestamps
        request.setStatus("PENDING");
        request.setCreatedAt(LocalDateTime.now());
        request.setUpdatedAt(LocalDateTime.now());
        
        // TODO: Add validation logic
        // TODO: Add notification logic
        // TODO: Add hospital assignment logic
    }

    @Override
    public String getNearestHospitalAddress(Double latitude, Double longitude) {
        String url = String.format(
            "https://maps.googleapis.com/maps/api/place/nearbysearch/json?location=%f,%f&radius=5000&type=hospital&key=%s",
            latitude, longitude, googleMapsApiKey
        );

        ResponseEntity<Map> response = restTemplate.getForEntity(url, Map.class);
        Map<String, Object> body = response.getBody();

        if (body != null && body.containsKey("results")) {
            List<Map<String, Object>> results = (List<Map<String, Object>>) body.get("results");
            if (!results.isEmpty()) {
                Map<String, Object> nearestHospital = results.get(0);
                return (String) nearestHospital.get("vicinity");
            }
        }

        return null;
    }
} 