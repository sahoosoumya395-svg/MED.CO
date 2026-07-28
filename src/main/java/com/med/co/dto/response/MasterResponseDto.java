package com.med.co.dto.response;

import java.time.LocalDate;
import java.time.LocalTime;

import com.med.co.enums.AppointmentStatus;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MasterResponseDto {

    private Long masterId;

    private String mrnNo;

    private Long patientId;
    private String patientName;

    private Long doctorId;
    private String doctorName;

    private Long departmentId;
    private String departmentName;

    private Long appointmentId;

    private LocalDate appointmentDate;

    private LocalTime appointmentTime;

    private AppointmentStatus status;

}