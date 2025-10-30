package com.example.patientservice.service;


import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

import com.example.patientservice.dto.PatientRequestDTO;
import com.example.patientservice.dto.PatientResponseDTO;
import com.example.patientservice.exception.EmailAlreadyExistsException;
import com.example.patientservice.exception.PatientNotFoundException;
import com.example.patientservice.mapper.PatientRequestMapper;
import com.example.patientservice.mapper.PatientResponseMapper;
import com.example.patientservice.model.Patient;
import com.example.patientservice.repository.PatientRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PatientService {

    private  final PatientRepository patientRepository;
    private  final PatientRequestMapper patientRequestMapper;
    private  final PatientResponseMapper patientResponseMapper;


  public List<PatientResponseDTO> getPatients() {
    List<Patient> patients = patientRepository.findAll();

    return patients.stream().map(patientResponseMapper::toDto).toList();
  }

  public PatientResponseDTO createPatient(PatientRequestDTO patientRequestDTO) {
    if (patientRepository.existsByEmail(patientRequestDTO.getEmail())) {
      throw new EmailAlreadyExistsException(
          "A patient with this email " + "already exists"
              + patientRequestDTO.getEmail());
    }

    Patient newPatient = patientRepository.save(
        patientRequestMapper.toEntity(patientRequestDTO));

    return patientResponseMapper.toDto(newPatient);
  }

  public PatientResponseDTO updatePatient(UUID id,
      PatientRequestDTO patientRequestDTO) {

    Patient patient = patientRepository.findById(id).orElseThrow(
        () -> new PatientNotFoundException("Patient not found with ID: " + id));

    if (patientRepository.existsByEmailAndIdNot(patientRequestDTO.getEmail(),
        id)) {
      throw new EmailAlreadyExistsException(
          "A patient with this email " + "already exists"
              + patientRequestDTO.getEmail());
    }

    patient.setName(patientRequestDTO.getName());
    patient.setAddress(patientRequestDTO.getAddress());
    patient.setEmail(patientRequestDTO.getEmail());
    patient.setDateOfBirth(LocalDate.parse(patientRequestDTO.getDateOfBirth()));

    Patient updatedPatient = patientRepository.save(patient);
    return patientResponseMapper.toDto(updatedPatient);
  }

  public void deletePatient(UUID id) {
    patientRepository.deleteById(id);
  }
}
