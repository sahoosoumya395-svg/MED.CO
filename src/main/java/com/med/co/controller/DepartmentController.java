package com.med.co.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import com.med.co.dto.request.DepartmentRequest;
import com.med.co.dto.response.ApiResponse;
import com.med.co.service.DepartmentService;

@RestController
@RequestMapping("/api/departments")
public class DepartmentController {

    @Autowired
    private DepartmentService departmentService;

    @PostMapping
    public ApiResponse<?> addDepartment(@RequestBody DepartmentRequest request) {
        return departmentService.addDepartment(request);
    }

    @GetMapping
    public ApiResponse<?> getAllDepartments() {
        return departmentService.getAllDepartments();
    }

    @GetMapping("/{id}")
    public ApiResponse<?> getDepartmentById(@PathVariable Long id) {
        return departmentService.getDepartmentById(id);
    }

    @PutMapping("/{id}")
    public ApiResponse<?> updateDepartment(@PathVariable Long id,
                                           @RequestBody DepartmentRequest request) {
        return departmentService.updateDepartment(id, request);
    }

    @DeleteMapping("/{id}")
    public ApiResponse<?> deleteDepartment(@PathVariable Long id) {
        return departmentService.deleteDepartment(id);
    }
    
    @GetMapping("/count")
    public ApiResponse<?> getTotalDepartments() {

        return departmentService.getTotalDepartments();

    }
}