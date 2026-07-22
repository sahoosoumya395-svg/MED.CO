package com.med.co.serviceimpl;

import java.util.List;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import com.med.co.dto.request.DepartmentRequest;
import com.med.co.dto.response.ApiResponse;
import com.med.co.dto.response.DepartmentResponse;
import com.med.co.entity.Department;
import com.med.co.repository.DepartmentRepository;
import com.med.co.service.DepartmentService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class DepartmentServiceImpl implements DepartmentService {

    private final DepartmentRepository departmentRepository;
    private final ModelMapper modelMapper;

    @Override
    public ApiResponse<DepartmentResponse> addDepartment(DepartmentRequest request) {
        try {

            if (departmentRepository.existsByDepartmentName(request.getDepartmentName())) {
                return new ApiResponse<>(400, "Department already exists", null);
            }

            Department department = modelMapper.map(request, Department.class);
            Department savedDepartment = departmentRepository.save(department);

            DepartmentResponse response =
                    modelMapper.map(savedDepartment, DepartmentResponse.class);

            return new ApiResponse<>(201, "Department added successfully", response);

        } catch (Exception e) {
            return new ApiResponse<>(500, e.getMessage(), null);
        }
    }

    @Override
    public ApiResponse<List<DepartmentResponse>> getAllDepartments() {
        try {

            List<DepartmentResponse> departments = departmentRepository.findAll()
                    .stream()
                    .map(department -> modelMapper.map(department, DepartmentResponse.class))
                    .collect(Collectors.toList());

            return new ApiResponse<>(200, "Departments fetched successfully", departments);

        } catch (Exception e) {
            return new ApiResponse<>(500, e.getMessage(), null);
        }
    }

    @Override
    public ApiResponse<DepartmentResponse> getDepartmentById(Long departmentId) {
        try {

            Department department = departmentRepository.findById(departmentId)
                    .orElseThrow(() -> new RuntimeException("Department not found"));

            DepartmentResponse response =
                    modelMapper.map(department, DepartmentResponse.class);

            return new ApiResponse<>(200, "Department fetched successfully", response);

        } catch (Exception e) {
            return new ApiResponse<>(500, e.getMessage(), null);
        }
    }

    @Override
    public ApiResponse<DepartmentResponse> updateDepartment(Long departmentId,
            DepartmentRequest request) {

        try {

            Department department = departmentRepository.findById(departmentId)
                    .orElseThrow(() -> new RuntimeException("Department not found"));

            department.setDepartmentName(request.getDepartmentName());

            Department updatedDepartment = departmentRepository.save(department);

            DepartmentResponse response =
                    modelMapper.map(updatedDepartment, DepartmentResponse.class);

            return new ApiResponse<>(200, "Department updated successfully", response);

        } catch (Exception e) {
            return new ApiResponse<>(500, e.getMessage(), null);
        }
    }

    @Override
    public ApiResponse<String> deleteDepartment(Long departmentId) {

        try {

            Department department = departmentRepository.findById(departmentId)
                    .orElseThrow(() -> new RuntimeException("Department not found"));

            departmentRepository.delete(department);

            return new ApiResponse<>(200, "Department deleted successfully", "Deleted");

        } catch (Exception e) {
            return new ApiResponse<>(500, e.getMessage(), null);
        }
    }
}