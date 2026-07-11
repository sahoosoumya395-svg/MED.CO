package com.med.co.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import com.med.co.dto.request.PatientRegistrationRequest;
import com.med.co.dto.response.ApiResponse;
import com.med.co.service.PatientService;

@RestController
@RequestMapping("/api/patient")
public class PatientController {

    @Autowired
    private PatientService patientService;

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
}