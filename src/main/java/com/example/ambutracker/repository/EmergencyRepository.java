package com.example.ambutracker.repository;

import com.example.ambutracker.entity.Ambulance;
import com.example.ambutracker.entity.Emergency;
import com.example.ambutracker.entity.EmergencyStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface EmergencyRepository extends JpaRepository<Emergency, Long> {
    List<Emergency> findByStatus(EmergencyStatus status);
    List<Emergency> findByAssignedAmbulance(Ambulance ambulance);
    List<Emergency> findByStatusOrderByCreatedAtDesc(EmergencyStatus status);
} 