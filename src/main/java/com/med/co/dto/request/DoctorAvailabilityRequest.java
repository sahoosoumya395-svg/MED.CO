package com.med.co.dto.request;

import java.time.LocalDate;
import java.time.LocalTime;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DoctorAvailabilityRequest {

    private Long doctorId;

    private LocalDate availableDate;

    private LocalTime startTime;

    private LocalTime endTime;

    private Boolean available;
}