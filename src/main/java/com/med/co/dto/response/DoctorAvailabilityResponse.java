package com.med.co.dto.response;

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
public class DoctorAvailabilityResponse {

    private Long availabilityId;

    private Long doctorId;

    private String doctorName;

    private LocalDate availableDate;

    private LocalTime startTime;

    private LocalTime endTime;

    private Boolean available;
}