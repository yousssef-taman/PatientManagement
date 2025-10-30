package com.example.patientservice.controller;


import com.example.patientservice.dto.PatientRequestDTO;
import com.example.patientservice.dto.PatientResponseDTO;
import com.example.patientservice.dto.validators.CreatePatientValidationGroup;
import com.example.patientservice.service.PatientService;
import jakarta.validation.groups.Default;
import java.util.List;
import java.util.UUID;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/patients")
@RequiredArgsConstructor
//@Tag(name = "Patient", description = "API for managing Patients")
public class PatientController {


  private final PatientService patientService;


  @GetMapping
  public ResponseEntity<List<PatientResponseDTO>> getPatients() {
    List<PatientResponseDTO> patients = patientService.getPatients();
    return ResponseEntity.ok().body(patients);
  }

  @PostMapping
  public ResponseEntity<PatientResponseDTO> createPatient(
      @Validated({Default.class, CreatePatientValidationGroup.class})
      @RequestBody PatientRequestDTO patientRequestDTO) {

    PatientResponseDTO patientResponseDTO = patientService.createPatient(
        patientRequestDTO);

    return ResponseEntity.ok().body(patientResponseDTO);
  }

  @PutMapping("/{id}")
  public ResponseEntity<PatientResponseDTO> updatePatient(@PathVariable UUID id,
      @Validated({Default.class}) @RequestBody PatientRequestDTO patientRequestDTO) {

    PatientResponseDTO patientResponseDTO = patientService.updatePatient(id,
        patientRequestDTO);

    return ResponseEntity.ok().body(patientResponseDTO);
  }

  @DeleteMapping("/{id}")
  public ResponseEntity<Void> deletePatient(@PathVariable UUID id) {
    patientService.deletePatient(id);
    return ResponseEntity.noContent().build();
  }
}
