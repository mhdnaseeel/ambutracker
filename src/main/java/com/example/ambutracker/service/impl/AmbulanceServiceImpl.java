package com.example.ambutracker.service.impl;

import com.example.ambutracker.entity.Ambulance;
import com.example.ambutracker.entity.AmbulanceStatus;
import com.example.ambutracker.entity.User;
import com.example.ambutracker.entity.UserType;
import com.example.ambutracker.repository.AmbulanceRepository;
import com.example.ambutracker.repository.UserRepository;
import com.example.ambutracker.service.AmbulanceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class AmbulanceServiceImpl implements AmbulanceService {

    @Autowired
    private AmbulanceRepository ambulanceRepository;

    @Autowired
    private UserRepository userRepository;

    @Override
    public Ambulance createAmbulance(Ambulance ambulance) {
        if (ambulanceRepository.existsByRegistrationNumber(ambulance.getRegistrationNumber())) {
            throw new RuntimeException("Ambulance with registration number " + ambulance.getRegistrationNumber() + " already exists");
        }
        ambulance.setStatus(AmbulanceStatus.AVAILABLE);
        return ambulanceRepository.save(ambulance);
    }

    @Override
    public Ambulance getAmbulanceById(Long id) {
        return ambulanceRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Ambulance not found with id: " + id));
    }

    @Override
    public Ambulance getAmbulanceByRegistrationNumber(String registrationNumber) {
        return ambulanceRepository.findByRegistrationNumber(registrationNumber)
                .orElseThrow(() -> new RuntimeException("Ambulance not found with registration number: " + registrationNumber));
    }

    @Override
    public List<Ambulance> getAllAmbulances() {
        return ambulanceRepository.findAll();
    }

    @Override
    public List<Ambulance> getAmbulancesByStatus(AmbulanceStatus status) {
        return ambulanceRepository.findByStatus(status);
    }

    @Override
    public List<Ambulance> getAmbulancesByDriver(User driver) {
        return ambulanceRepository.findByDriver(driver);
    }

    @Override
    public Ambulance updateAmbulance(Long id, Ambulance ambulanceDetails) {
        Ambulance ambulance = getAmbulanceById(id);
        
        if (!ambulance.getRegistrationNumber().equals(ambulanceDetails.getRegistrationNumber()) &&
            ambulanceRepository.existsByRegistrationNumber(ambulanceDetails.getRegistrationNumber())) {
            throw new RuntimeException("Ambulance with registration number " + ambulanceDetails.getRegistrationNumber() + " already exists");
        }

        ambulance.setRegistrationNumber(ambulanceDetails.getRegistrationNumber());
        ambulance.setModel(ambulanceDetails.getModel());
        ambulance.setVehicleType(ambulanceDetails.getVehicleType());
        ambulance.setEquipmentStatus(ambulanceDetails.getEquipmentStatus());
        ambulance.setCurrentLocation(ambulanceDetails.getCurrentLocation());
        ambulance.setLatitude(ambulanceDetails.getLatitude());
        ambulance.setLongitude(ambulanceDetails.getLongitude());
        ambulance.setStatus(ambulanceDetails.getStatus());

        return ambulanceRepository.save(ambulance);
    }

    @Override
    public Ambulance updateAmbulanceStatus(Long id, AmbulanceStatus status) {
        Ambulance ambulance = getAmbulanceById(id);
        ambulance.setStatus(status);
        return ambulanceRepository.save(ambulance);
    }

    @Override
    public Ambulance updateAmbulanceLocation(Long id, Double latitude, Double longitude) {
        Ambulance ambulance = getAmbulanceById(id);
        ambulance.setLatitude(latitude);
        ambulance.setLongitude(longitude);
        return ambulanceRepository.save(ambulance);
    }

    @Override
    public Ambulance assignDriver(Long ambulanceId, Long driverId) {
        Ambulance ambulance = getAmbulanceById(ambulanceId);
        User driver = userRepository.findById(driverId)
                .orElseThrow(() -> new RuntimeException("Driver not found with id: " + driverId));

        if (driver.getUserType() != UserType.AMBULANCE_DRIVER) {
            throw new RuntimeException("User with id " + driverId + " is not a driver");
        }

        ambulance.setDriver(driver);
        return ambulanceRepository.save(ambulance);
    }

    @Override
    public void deleteAmbulance(Long id) {
        if (!ambulanceRepository.existsById(id)) {
            throw new RuntimeException("Ambulance not found with id: " + id);
        }
        ambulanceRepository.deleteById(id);
    }

    @Override
    public List<Ambulance> findNearbyAmbulances(Double latitude, Double longitude, Double radiusInKm) {
        // Get all available ambulances
        List<Ambulance> availableAmbulances = ambulanceRepository.findByStatus(AmbulanceStatus.AVAILABLE);
        
        // Filter ambulances within the radius
        return availableAmbulances.stream()
                .filter(ambulance -> isWithinRadius(
                        latitude, longitude,
                        ambulance.getLatitude(), ambulance.getLongitude(),
                        radiusInKm))
                .collect(Collectors.toList());
    }

    private boolean isWithinRadius(Double lat1, Double lon1, Double lat2, Double lon2, Double radiusInKm) {
        final int R = 6371; // Earth's radius in kilometers

        double latDistance = Math.toRadians(lat2 - lat1);
        double lonDistance = Math.toRadians(lon2 - lon1);
        double a = Math.sin(latDistance / 2) * Math.sin(latDistance / 2)
                + Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2))
                * Math.sin(lonDistance / 2) * Math.sin(lonDistance / 2);
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
        double distance = R * c;

        return distance <= radiusInKm;
    }
} 