package com.example.patientservice.model;

import jakarta.persistence.*;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.time.LocalDate;
import java.util.UUID;

@Entity
@Getter
@Setter
@ToString
@RequiredArgsConstructor
public class Patient {
  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  private UUID id;

  @NotNull
  private String name;

  @NotNull
  @Email
  @Column(unique = true)
  private String email;

  @NotNull
  private String address;

  @NotNull
  private LocalDate dateOfBirth;

  @NotNull
  private LocalDate registeredDate;

}
