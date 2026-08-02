package com.med.co.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class DepartmentPatientCountResponse {

    private String departmentName;
    private long patientCount;
}
