package com.med.co.controller;

import org.springframework.web.bind.annotation.*;

import com.med.co.dto.request.DepartmentPatientCountRequest;
import com.med.co.dto.request.PatientRegistrationRequest;
import com.med.co.dto.response.ApiResponse;
import com.med.co.service.PatientService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/patient")
@CrossOrigin(origins = "http://localhost:4200")
public class PatientController {

    private final PatientService patientService;

    PatientController(PatientService patientService) {
        this.patientService = patientService;
    }

    @PostMapping("/register")
    public ApiResponse<?> registerPatient(@Valid @RequestBody PatientRegistrationRequest request) {
        return patientService.registerPatient(request);
    }

    @GetMapping("/all")
    public ApiResponse<?> getAllPatients(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "patientId") String sortBy,
            @RequestParam(defaultValue = "asc") String direction) {

        return patientService.getAllPatients(page, size, sortBy, direction);
    }

    @GetMapping("/count")
    public ApiResponse<?> countAllPatients() {
        return patientService.countAllPatients();
    }

    @PostMapping("/count-by-department")
    public ApiResponse<?> countPatientsByDepartment(
            @Valid @RequestBody DepartmentPatientCountRequest request) {

        return patientService.countPatientsByDepartment(request);
    }

    @GetMapping("/{id}")
    public ApiResponse<?> getPatientById(@PathVariable Long id) {
        return patientService.getPatientById(id);
    }

    @PutMapping("/update/{id}")
    public ApiResponse<?> updatePatient(
            @PathVariable Long id,
            @Valid @RequestBody PatientRegistrationRequest request) {

        return patientService.updatePatient(id, request);
    }

    @DeleteMapping("/delete/{id}")
    public ApiResponse<?> deletePatient(@PathVariable Long id) {
        return patientService.deletePatient(id);
    }
    
    @GetMapping("/mrn/{mrnNo}")
    public ApiResponse<?> getPatientByMrn(@PathVariable String mrnNo) {
        return patientService.getPatientByMrn(mrnNo);
    }
    
    
    
    
}