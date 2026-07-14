package com.med.co.controller;

import org.springframework.web.bind.annotation.*;

import com.med.co.dto.request.PatientRegistrationRequest;
import com.med.co.dto.response.ApiResponse;
import com.med.co.service.PatientService;

@RestController
@RequestMapping("/api/patient")
public class PatientController {

    private final PatientService patientService;

    PatientController(PatientService patientService) {
        this.patientService = patientService;
    }

    @PostMapping("/register")
    public ApiResponse<?> registerPatient(@RequestBody PatientRegistrationRequest request) {
        return patientService.registerPatient(request);
    }

    @GetMapping("/all")
    public ApiResponse<?> getAllPatients() {
        return patientService.getAllPatients();
    }

    @GetMapping("/{id}")
    public ApiResponse<?> getPatientById(@PathVariable Long id) {
        return patientService.getPatientById(id);
    }

    @PutMapping("/update/{id}")
    public ApiResponse<?> updatePatient(@PathVariable Long id,
                                        @RequestBody PatientRegistrationRequest request) {
        return patientService.updatePatient(id, request);
    }

    @DeleteMapping("/delete/{id}")
    public ApiResponse<?> deletePatient(@PathVariable Long id) {
        return patientService.deletePatient(id);
    }
}