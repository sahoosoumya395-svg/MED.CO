package com.med.co.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PrescriptionRequestDto {

    @NotNull(message = "Appointment Id is required")
    private Long appointmentId;

    @NotBlank(message = "Diagnosis is required")
    @Size(min = 5, max = 500, message = "Diagnosis must be between 5 and 500 characters")
    private String diagnosis;

    @NotBlank(message = "Medicines are required")
    @Size(min = 5, max = 1000, message = "Medicines must be between 5 and 1000 characters")
    private String medicines;

    @NotBlank(message = "Advice is required")
    @Size(min = 5, max = 500, message = "Advice must be between 5 and 500 characters")
    private String advice;

}