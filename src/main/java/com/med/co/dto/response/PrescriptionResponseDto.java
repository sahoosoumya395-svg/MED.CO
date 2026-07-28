package com.med.co.dto.response;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PrescriptionResponseDto {

    private Long prescriptionId;

    private Long appointmentId;

    private Long doctorId;
    private String doctorName;

    private Long patientId;
    private String patientName;

    // Patient Medical Record Number
    private String mrnNo;

    private String diagnosis;

    private String medicines;

    private String advice;

    // Stored HTML of the prescription
    private String prescriptionHtml;

    private LocalDateTime createdAt;

}