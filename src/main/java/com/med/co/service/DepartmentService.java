package com.med.co.service;

import java.util.List;

import com.med.co.dto.request.DepartmentRequest;
import com.med.co.dto.response.ApiResponse;

public interface DepartmentService {

    ApiResponse addDepartment(DepartmentRequest request);

    ApiResponse getAllDepartments();

    ApiResponse getDepartmentById(Long departmentId);

    ApiResponse updateDepartment(Long departmentId, DepartmentRequest request);

    ApiResponse deleteDepartment(Long departmentId);
}