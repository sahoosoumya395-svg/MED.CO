package com.med.co.controller;

import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.web.bind.annotation.*;
import jakarta.validation.Valid;

import com.med.co.dto.request.DepartmentRequest;
import com.med.co.dto.response.ApiResponse;
import com.med.co.service.DepartmentService;

@RestController
@RequestMapping("/api/departments")
public class DepartmentController {

    @Autowired
    private DepartmentService departmentService;

    // Create Department
    @PostMapping("/add-dept")
    public ApiResponse<?> addDepartment(@Valid @RequestBody DepartmentRequest request) {
        return departmentService.addDepartment(request);
    }

    // Get All Departments
    @GetMapping("/getAll")
    public ApiResponse<?> getAllDepartments() {
        return departmentService.getAllDepartments();
    }

    // Get Department By Id
    @GetMapping("/get/{id}")
    public ApiResponse<?> getDepartmentById(@PathVariable Long id) {
        return departmentService.getDepartmentById(id);
    }

    // Update Department
    @PutMapping("/update/{id}")
    public ApiResponse<?> updateDepartment(@PathVariable Long id,
                                           @Valid @RequestBody DepartmentRequest request) {
        return departmentService.updateDepartment(id, request);
    }

    // Delete Department
    @DeleteMapping("/delete/{id}")
    public ApiResponse<?> deleteDepartment(@PathVariable Long id) {
        return departmentService.deleteDepartment(id);
    }

    // Get Total Departments Count
    @GetMapping("/count")
    public ApiResponse<?> getTotalDepartments() {
        return departmentService.getTotalDepartments();
    }
}