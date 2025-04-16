package com.example.ambutracker.service;

import com.example.ambutracker.dto.EmergencyRequestDTO;
import com.example.ambutracker.entity.Emergency;
import com.example.ambutracker.entity.EmergencyStatus;
import com.example.ambutracker.entity.User;
import com.example.ambutracker.model.EmergencyRequest;

import java.util.List;

public interface EmergencyService {
    Emergency createEmergencyRequest(EmergencyRequestDTO requestDTO, User requester);
    Emergency getEmergencyById(Long id);
    List<Emergency> getAllEmergencies();
    List<Emergency> getEmergenciesByStatus(EmergencyStatus status);
    List<Emergency> getEmergenciesByDispatcher(User dispatcher);
    Emergency assignAmbulance(Long emergencyId, Long ambulanceId);
    Emergency updateEmergencyStatus(Long id, EmergencyStatus status);
    Emergency updateEmergencyLocation(Long id, Double latitude, Double longitude);
    void deleteEmergency(Long id);
    List<Emergency> findNearbyEmergencies(Double latitude, Double longitude, Double radiusInKm);
    void processEmergencyRequest(EmergencyRequest request);
    String getNearestHospitalAddress(Double latitude, Double longitude);
} 