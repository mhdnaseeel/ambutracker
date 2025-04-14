package com.example.ambutracker.service;

import com.example.ambutracker.entity.Ambulance;
import com.example.ambutracker.entity.AmbulanceStatus;
import com.example.ambutracker.entity.User;

import java.util.List;

public interface AmbulanceService {
    Ambulance createAmbulance(Ambulance ambulance);
    Ambulance getAmbulanceById(Long id);
    Ambulance getAmbulanceByRegistrationNumber(String registrationNumber);
    List<Ambulance> getAllAmbulances();
    List<Ambulance> getAmbulancesByStatus(AmbulanceStatus status);
    List<Ambulance> getAmbulancesByDriver(User driver);
    Ambulance updateAmbulance(Long id, Ambulance ambulance);
    Ambulance updateAmbulanceStatus(Long id, AmbulanceStatus status);
    Ambulance updateAmbulanceLocation(Long id, Double latitude, Double longitude);
    Ambulance assignDriver(Long ambulanceId, Long driverId);
    void deleteAmbulance(Long id);
    List<Ambulance> findNearbyAmbulances(Double latitude, Double longitude, Double radiusInKm);
} 