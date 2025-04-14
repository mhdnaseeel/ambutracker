package com.example.ambutracker.service.impl;

import com.example.ambutracker.entity.Ambulance;
import com.example.ambutracker.entity.AmbulanceStatus;
import com.example.ambutracker.entity.User;
import com.example.ambutracker.entity.UserType;
import com.example.ambutracker.repository.AmbulanceRepository;
import com.example.ambutracker.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AmbulanceServiceImplTest {

    @Mock
    private AmbulanceRepository ambulanceRepository;

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private AmbulanceServiceImpl ambulanceService;

    private Ambulance testAmbulance;
    private User testDriver;

    @BeforeEach
    void setUp() {
        testAmbulance = new Ambulance();
        testAmbulance.setId(1L);
        testAmbulance.setRegistrationNumber("AMB001");
        testAmbulance.setModel("Toyota HiAce");
        testAmbulance.setVehicleType("Basic Life Support");
        testAmbulance.setEquipmentStatus("Fully Equipped");
        testAmbulance.setCurrentLocation("Main Street");
        testAmbulance.setLatitude(1.234);
        testAmbulance.setLongitude(5.678);
        testAmbulance.setStatus(AmbulanceStatus.AVAILABLE);

        testDriver = new User();
        testDriver.setId(1L);
        testDriver.setUsername("driver1");
        testDriver.setUserType(UserType.AMBULANCE_DRIVER);
    }

    @Test
    void createAmbulance_Success() {
        when(ambulanceRepository.existsByRegistrationNumber(anyString())).thenReturn(false);
        when(ambulanceRepository.save(any(Ambulance.class))).thenReturn(testAmbulance);

        Ambulance result = ambulanceService.createAmbulance(testAmbulance);

        assertNotNull(result);
        assertEquals(AmbulanceStatus.AVAILABLE, result.getStatus());
        verify(ambulanceRepository).save(any(Ambulance.class));
    }

    @Test
    void createAmbulance_DuplicateRegistrationNumber() {
        when(ambulanceRepository.existsByRegistrationNumber(anyString())).thenReturn(true);

        assertThrows(RuntimeException.class, () -> ambulanceService.createAmbulance(testAmbulance));
        verify(ambulanceRepository, never()).save(any(Ambulance.class));
    }

    @Test
    void getAmbulanceById_Success() {
        when(ambulanceRepository.findById(1L)).thenReturn(Optional.of(testAmbulance));

        Ambulance result = ambulanceService.getAmbulanceById(1L);

        assertNotNull(result);
        assertEquals(1L, result.getId());
    }

    @Test
    void getAmbulanceById_NotFound() {
        when(ambulanceRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class, () -> ambulanceService.getAmbulanceById(1L));
    }

    @Test
    void getAmbulanceByRegistrationNumber_Success() {
        when(ambulanceRepository.findByRegistrationNumber("AMB001")).thenReturn(Optional.of(testAmbulance));

        Ambulance result = ambulanceService.getAmbulanceByRegistrationNumber("AMB001");

        assertNotNull(result);
        assertEquals("AMB001", result.getRegistrationNumber());
    }

    @Test
    void getAmbulanceByRegistrationNumber_NotFound() {
        when(ambulanceRepository.findByRegistrationNumber(anyString())).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class, () -> ambulanceService.getAmbulanceByRegistrationNumber("AMB001"));
    }

    @Test
    void getAllAmbulances_Success() {
        List<Ambulance> ambulances = Arrays.asList(testAmbulance);
        when(ambulanceRepository.findAll()).thenReturn(ambulances);

        List<Ambulance> result = ambulanceService.getAllAmbulances();

        assertNotNull(result);
        assertEquals(1, result.size());
    }

    @Test
    void getAmbulancesByStatus_Success() {
        List<Ambulance> ambulances = Arrays.asList(testAmbulance);
        when(ambulanceRepository.findByStatus(AmbulanceStatus.AVAILABLE)).thenReturn(ambulances);

        List<Ambulance> result = ambulanceService.getAmbulancesByStatus(AmbulanceStatus.AVAILABLE);

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(AmbulanceStatus.AVAILABLE, result.get(0).getStatus());
    }

    @Test
    void getAmbulancesByDriver_Success() {
        List<Ambulance> ambulances = Arrays.asList(testAmbulance);
        when(ambulanceRepository.findByDriver(testDriver)).thenReturn(ambulances);

        List<Ambulance> result = ambulanceService.getAmbulancesByDriver(testDriver);

        assertNotNull(result);
        assertEquals(1, result.size());
    }

    @Test
    void updateAmbulance_Success() {
        Ambulance updatedAmbulance = new Ambulance();
        updatedAmbulance.setRegistrationNumber("AMB002");
        updatedAmbulance.setModel("Updated Model");
        updatedAmbulance.setStatus(AmbulanceStatus.ON_DUTY);

        when(ambulanceRepository.findById(1L)).thenReturn(Optional.of(testAmbulance));
        when(ambulanceRepository.existsByRegistrationNumber("AMB002")).thenReturn(false);
        when(ambulanceRepository.save(any(Ambulance.class))).thenReturn(updatedAmbulance);

        Ambulance result = ambulanceService.updateAmbulance(1L, updatedAmbulance);

        assertNotNull(result);
        assertEquals("AMB002", result.getRegistrationNumber());
        assertEquals("Updated Model", result.getModel());
        assertEquals(AmbulanceStatus.ON_DUTY, result.getStatus());
    }

    @Test
    void updateAmbulanceStatus_Success() {
        when(ambulanceRepository.findById(1L)).thenReturn(Optional.of(testAmbulance));
        when(ambulanceRepository.save(any(Ambulance.class))).thenReturn(testAmbulance);

        Ambulance result = ambulanceService.updateAmbulanceStatus(1L, AmbulanceStatus.ON_DUTY);

        assertNotNull(result);
        assertEquals(AmbulanceStatus.ON_DUTY, result.getStatus());
    }

    @Test
    void updateAmbulanceLocation_Success() {
        when(ambulanceRepository.findById(1L)).thenReturn(Optional.of(testAmbulance));
        when(ambulanceRepository.save(any(Ambulance.class))).thenReturn(testAmbulance);

        Ambulance result = ambulanceService.updateAmbulanceLocation(1L, 2.345, 6.789);

        assertNotNull(result);
        assertEquals(2.345, result.getLatitude());
        assertEquals(6.789, result.getLongitude());
    }

    @Test
    void assignDriver_Success() {
        when(ambulanceRepository.findById(1L)).thenReturn(Optional.of(testAmbulance));
        when(userRepository.findById(1L)).thenReturn(Optional.of(testDriver));
        when(ambulanceRepository.save(any(Ambulance.class))).thenReturn(testAmbulance);

        Ambulance result = ambulanceService.assignDriver(1L, 1L);

        assertNotNull(result);
        assertEquals(testDriver, result.getDriver());
    }

    @Test
    void assignDriver_InvalidDriverType() {
        User nonDriver = new User();
        nonDriver.setId(1L);
        nonDriver.setUserType(UserType.MEDICAL_STAFF);

        when(ambulanceRepository.findById(1L)).thenReturn(Optional.of(testAmbulance));
        when(userRepository.findById(1L)).thenReturn(Optional.of(nonDriver));

        assertThrows(RuntimeException.class, () -> ambulanceService.assignDriver(1L, 1L));
    }

    @Test
    void deleteAmbulance_Success() {
        when(ambulanceRepository.existsById(1L)).thenReturn(true);
        doNothing().when(ambulanceRepository).deleteById(1L);

        assertDoesNotThrow(() -> ambulanceService.deleteAmbulance(1L));
        verify(ambulanceRepository).deleteById(1L);
    }

    @Test
    void deleteAmbulance_NotFound() {
        when(ambulanceRepository.existsById(1L)).thenReturn(false);

        assertThrows(RuntimeException.class, () -> ambulanceService.deleteAmbulance(1L));
        verify(ambulanceRepository, never()).deleteById(anyLong());
    }

    @Test
    void findNearbyAmbulances_Success() {
        List<Ambulance> availableAmbulances = Arrays.asList(testAmbulance);
        when(ambulanceRepository.findByStatus(AmbulanceStatus.AVAILABLE)).thenReturn(availableAmbulances);

        List<Ambulance> result = ambulanceService.findNearbyAmbulances(1.234, 5.678, 10.0);

        assertNotNull(result);
        assertEquals(1, result.size());
    }
} 