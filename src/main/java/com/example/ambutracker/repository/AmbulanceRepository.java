package com.example.ambutracker.repository;

import com.example.ambutracker.entity.Ambulance;
import com.example.ambutracker.entity.AmbulanceStatus;
import com.example.ambutracker.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface AmbulanceRepository extends JpaRepository<Ambulance, Long> {
    Optional<Ambulance> findByRegistrationNumber(String registrationNumber);
    List<Ambulance> findByStatus(AmbulanceStatus status);
    List<Ambulance> findByDriver(User driver);
    boolean existsByRegistrationNumber(String registrationNumber);
} 