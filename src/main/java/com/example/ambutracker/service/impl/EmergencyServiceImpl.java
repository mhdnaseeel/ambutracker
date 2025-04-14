package com.example.ambutracker.service.impl;

import com.example.ambutracker.dto.EmergencyRequestDTO;
import com.example.ambutracker.entity.Emergency;
import com.example.ambutracker.entity.EmergencyStatus;
import com.example.ambutracker.entity.User;
import com.example.ambutracker.repository.EmergencyRepository;
import com.example.ambutracker.service.EmergencyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import jakarta.persistence.EntityNotFoundException;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class EmergencyServiceImpl implements EmergencyService {

    private final EmergencyRepository emergencyRepository;

    @Autowired
    public EmergencyServiceImpl(EmergencyRepository emergencyRepository) {
        this.emergencyRepository = emergencyRepository;
    }

    @Override
    @Transactional
    public Emergency createEmergencyRequest(EmergencyRequestDTO requestDTO, User requester) {
        Emergency emergency = new Emergency();
        emergency.setRequester(requester);
        emergency.setDescription(requestDTO.getDescription());
        emergency.setLatitude(requestDTO.getLatitude());
        emergency.setLongitude(requestDTO.getLongitude());
        emergency.setStatus(EmergencyStatus.PENDING);
        emergency.setCreatedAt(LocalDateTime.now());
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
        // TODO: Implement nearby emergencies search using spatial queries
        // This is a placeholder implementation
        return emergencyRepository.findAll(PageRequest.of(0, 10, Sort.by(Sort.Direction.DESC, "createdAt"))).getContent();
    }
} 