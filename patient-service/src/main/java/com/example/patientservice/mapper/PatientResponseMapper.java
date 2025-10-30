package com.example.patientservice.mapper;

import com.example.patientservice.dto.PatientResponseDTO;
import com.example.patientservice.model.Patient;
import org.mapstruct.Mapper;
import org.springframework.stereotype.Component;

@Mapper(componentModel = "spring")
@Component
public interface PatientResponseMapper {
    PatientResponseDTO toDto(Patient patient);
    Patient toEntity(PatientResponseDTO patientResponseDTO);
}
