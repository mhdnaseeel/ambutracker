package com.example.ambutracker.service.impl;

import com.example.ambutracker.dto.EmergencyRequestDTO;
import com.example.ambutracker.entity.Emergency;
import com.example.ambutracker.entity.EmergencyStatus;
import com.example.ambutracker.entity.User;
import com.example.ambutracker.repository.EmergencyRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;

import jakarta.persistence.EntityNotFoundException;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class EmergencyServiceImplTest {

    @Mock
    private EmergencyRepository emergencyRepository;

    @InjectMocks
    private EmergencyServiceImpl emergencyService;

    private Emergency mockEmergency;
    private User mockUser;
    private EmergencyRequestDTO mockRequestDTO;

    @BeforeEach
    void setUp() {
        mockUser = new User();
        mockUser.setId(1L);
        mockUser.setUsername("testuser");

        mockEmergency = new Emergency();
        mockEmergency.setId(1L);
        mockEmergency.setRequester(mockUser);
        mockEmergency.setDescription("Test Emergency");
        mockEmergency.setLatitude(40.7128);
        mockEmergency.setLongitude(-74.0060);
        mockEmergency.setStatus(EmergencyStatus.PENDING);
        mockEmergency.setCreatedAt(LocalDateTime.now());

        mockRequestDTO = new EmergencyRequestDTO();
        mockRequestDTO.setDescription("Test Emergency");
        mockRequestDTO.setLatitude(40.7128);
        mockRequestDTO.setLongitude(-74.0060);
    }

    @Test
    void createEmergencyRequest_Success() {
        when(emergencyRepository.save(any(Emergency.class))).thenReturn(mockEmergency);

        Emergency result = emergencyService.createEmergencyRequest(mockRequestDTO, mockUser);

        assertNotNull(result);
        assertEquals(mockRequestDTO.getDescription(), result.getDescription());
        assertEquals(mockRequestDTO.getLatitude(), result.getLatitude());
        assertEquals(mockRequestDTO.getLongitude(), result.getLongitude());
        assertEquals(EmergencyStatus.PENDING, result.getStatus());
        assertEquals(mockUser, result.getRequester());
        verify(emergencyRepository, times(1)).save(any(Emergency.class));
    }

    @Test
    void getEmergencyById_Success() {
        when(emergencyRepository.findById(1L)).thenReturn(Optional.of(mockEmergency));

        Emergency result = emergencyService.getEmergencyById(1L);

        assertNotNull(result);
        assertEquals(mockEmergency.getId(), result.getId());
        verify(emergencyRepository, times(1)).findById(1L);
    }

    @Test
    void getEmergencyById_NotFound() {
        when(emergencyRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> {
            emergencyService.getEmergencyById(1L);
        });
        verify(emergencyRepository, times(1)).findById(1L);
    }

    @Test
    void getAllEmergencies_Success() {
        List<Emergency> mockEmergencies = Arrays.asList(mockEmergency);
        when(emergencyRepository.findAll(any(Sort.class))).thenReturn(mockEmergencies);

        List<Emergency> result = emergencyService.getAllEmergencies();

        assertNotNull(result);
        assertEquals(1, result.size());
        verify(emergencyRepository, times(1)).findAll(any(Sort.class));
    }

    @Test
    void getEmergenciesByStatus_Success() {
        List<Emergency> mockEmergencies = Arrays.asList(mockEmergency);
        when(emergencyRepository.findByStatus(EmergencyStatus.PENDING)).thenReturn(mockEmergencies);

        List<Emergency> result = emergencyService.getEmergenciesByStatus(EmergencyStatus.PENDING);

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(EmergencyStatus.PENDING, result.get(0).getStatus());
        verify(emergencyRepository, times(1)).findByStatus(EmergencyStatus.PENDING);
    }

    @Test
    void getEmergenciesByDispatcher_Success() {
        List<Emergency> mockEmergencies = Arrays.asList(mockEmergency);
        when(emergencyRepository.findByStatus(EmergencyStatus.PENDING)).thenReturn(mockEmergencies);

        List<Emergency> result = emergencyService.getEmergenciesByDispatcher(mockUser);

        assertNotNull(result);
        assertEquals(1, result.size());
        verify(emergencyRepository, times(1)).findByStatus(EmergencyStatus.PENDING);
    }

    @Test
    void updateEmergencyStatus_Success() {
        when(emergencyRepository.findById(1L)).thenReturn(Optional.of(mockEmergency));
        when(emergencyRepository.save(any(Emergency.class))).thenReturn(mockEmergency);

        Emergency result = emergencyService.updateEmergencyStatus(1L, EmergencyStatus.IN_PROGRESS);

        assertNotNull(result);
        assertEquals(EmergencyStatus.IN_PROGRESS, result.getStatus());
        verify(emergencyRepository, times(1)).findById(1L);
        verify(emergencyRepository, times(1)).save(any(Emergency.class));
    }

    @Test
    void updateEmergencyLocation_Success() {
        when(emergencyRepository.findById(1L)).thenReturn(Optional.of(mockEmergency));
        when(emergencyRepository.save(any(Emergency.class))).thenReturn(mockEmergency);

        Double newLatitude = 40.7589;
        Double newLongitude = -73.9851;

        Emergency result = emergencyService.updateEmergencyLocation(1L, newLatitude, newLongitude);

        assertNotNull(result);
        assertEquals(newLatitude, result.getLatitude());
        assertEquals(newLongitude, result.getLongitude());
        verify(emergencyRepository, times(1)).findById(1L);
        verify(emergencyRepository, times(1)).save(any(Emergency.class));
    }

    @Test
    void deleteEmergency_Success() {
        when(emergencyRepository.findById(1L)).thenReturn(Optional.of(mockEmergency));
        doNothing().when(emergencyRepository).delete(any(Emergency.class));

        emergencyService.deleteEmergency(1L);

        verify(emergencyRepository, times(1)).findById(1L);
        verify(emergencyRepository, times(1)).delete(any(Emergency.class));
    }

    @Test
    void findNearbyEmergencies_Success() {
        List<Emergency> mockEmergencies = Arrays.asList(mockEmergency);
        when(emergencyRepository.findAll(any(PageRequest.class))).thenReturn(new PageImpl<>(mockEmergencies));

        List<Emergency> result = emergencyService.findNearbyEmergencies(40.7128, -74.0060, 5.0);

        assertNotNull(result);
        assertEquals(1, result.size());
        verify(emergencyRepository, times(1)).findAll(any(PageRequest.class));
    }
} 