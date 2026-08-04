package com.med.co.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class DepartmentPatientCountRequest {

    @NotBlank(message = "Department name is required")
    private String departmentName;
}
