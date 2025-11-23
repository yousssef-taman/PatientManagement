package com.example.patientservice.service;

import com.example.patientservice.dto.PatientRequestDTO;
import com.example.patientservice.dto.PatientResponseDTO;
import com.example.patientservice.exception.EmailAlreadyExistsException;
import com.example.patientservice.exception.PatientNotFoundException;
import com.example.patientservice.mapper.PatientRequestMapper;
import com.example.patientservice.mapper.PatientResponseMapper;
import com.example.patientservice.model.Patient;
import com.example.patientservice.repository.PatientRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class PatientServiceTest {

    @Mock
    private PatientRepository patientRepository;

    @Mock
    private PatientRequestMapper patientRequestMapper;

    @Mock
    private PatientResponseMapper patientResponseMapper;

    @InjectMocks
    private PatientService patientService;

    private Patient patient;
    private PatientRequestDTO patientRequestDTO;
    private PatientResponseDTO patientResponseDTO;
    private UUID patientId;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        patientId = UUID.randomUUID();

        patientRequestDTO = new PatientRequestDTO();
        patientRequestDTO.setName("John Doe");
        patientRequestDTO.setEmail("john@example.com");
        patientRequestDTO.setAddress("123 Street");
        patientRequestDTO.setDateOfBirth("1990-01-01");

        patient = new Patient();
        patient.setId(patientId);
        patient.setName("John Doe");
        patient.setEmail("john@example.com");
        patient.setAddress("123 Street");
        patient.setDateOfBirth(LocalDate.of(1990, 1, 1));

        patientResponseDTO = new PatientResponseDTO();
        patientResponseDTO.setId(String.valueOf(patientId));
        patientResponseDTO.setName("John Doe");
        patientResponseDTO.setEmail("john@example.com");
        patientResponseDTO.setAddress("123 Street");
        patientResponseDTO.setDateOfBirth("1990-01-01");
    }

    @Test
    void testGetPatients() {
        when(patientRepository.findAll()).thenReturn(List.of(patient));
        when(patientResponseMapper.toDto(patient)).thenReturn(patientResponseDTO);

        List<PatientResponseDTO> result = patientService.getPatients();

        assertEquals(1, result.size());
        assertEquals("John Doe", result.get(0).getName());
    }

    @Test
    void testCreatePatientSuccess() {
        when(patientRepository.existsByEmail(patientRequestDTO.getEmail())).thenReturn(false);
        when(patientRequestMapper.toEntity(patientRequestDTO)).thenReturn(patient);
        when(patientRepository.save(patient)).thenReturn(patient);
        when(patientResponseMapper.toDto(patient)).thenReturn(patientResponseDTO);

        PatientResponseDTO result = patientService.createPatient(patientRequestDTO);

        assertEquals("John Doe", result.getName());
    }

    @Test
    void testCreatePatientEmailAlreadyExists() {
        when(patientRepository.existsByEmail(patientRequestDTO.getEmail())).thenReturn(true);

        assertThrows(EmailAlreadyExistsException.class, () -> patientService.createPatient(patientRequestDTO));
        verify(patientRepository, never()).save(any());
    }

    @Test
    void testUpdatePatientSuccess() {
        when(patientRepository.findById(patientId)).thenReturn(Optional.of(patient));
        when(patientRepository.existsByEmailAndIdNot(patientRequestDTO.getEmail(), patientId))
                .thenReturn(false);
        when(patientRepository.save(patient)).thenReturn(patient);
        when(patientResponseMapper.toDto(patient)).thenReturn(patientResponseDTO);

        PatientResponseDTO result = patientService.updatePatient(patientId, patientRequestDTO);

        assertEquals("John Doe", result.getName());
    }

    @Test
    void testUpdatePatientNotFound() {
        when(patientRepository.findById(patientId)).thenReturn(Optional.empty());

        assertThrows(PatientNotFoundException.class,
                () -> patientService.updatePatient(patientId, patientRequestDTO));
    }

    @Test
    void testUpdatePatientEmailAlreadyExists() {
        when(patientRepository.findById(patientId)).thenReturn(Optional.of(patient));
        when(patientRepository.existsByEmailAndIdNot(patientRequestDTO.getEmail(), patientId))
                .thenReturn(true);

        assertThrows(EmailAlreadyExistsException.class,
                () -> patientService.updatePatient(patientId, patientRequestDTO));
    }


}
