package com.med.co.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DepartmentAvailabilityResponseDto {

    private Long departmentId;

    private String departmentName;

    private Long totalDoctors;

    private Long availableDoctors;

}