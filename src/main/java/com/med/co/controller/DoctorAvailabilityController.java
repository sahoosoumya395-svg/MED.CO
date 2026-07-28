package com.med.co.controller;

import java.time.LocalDate;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.med.co.dto.request.DoctorAvailabilityRequest;
import com.med.co.dto.response.ApiResponse;
import com.med.co.dto.response.AvailableDoctorsCountResponse;
import com.med.co.service.DoctorAvailabilityService;

@RestController
@RequestMapping("/api/doctor-availability")
public class DoctorAvailabilityController {

    @Autowired
    private DoctorAvailabilityService doctorAvailabilityService;

    // Add doctor availability
    @PostMapping
    public ApiResponse<?> addAvailability(@RequestBody DoctorAvailabilityRequest request) {
        return doctorAvailabilityService.addAvailability(request);
    }

    // Get all availability of a doctor
    @GetMapping("/{doctorId}")
    public ApiResponse<?> getAvailabilityByDoctor(@PathVariable Long doctorId) {
        return doctorAvailabilityService.getAvailabilityByDoctor(doctorId);
    }

    // Update availability
    @PutMapping("/{availabilityId}")
    public ApiResponse<?> updateAvailability(
            @PathVariable Long availabilityId,
            @RequestBody DoctorAvailabilityRequest request) {

        return doctorAvailabilityService.updateAvailability(availabilityId, request);
    }

    // Delete availability
    @DeleteMapping("/{availabilityId}")
    public ApiResponse<?> deleteAvailability(@PathVariable Long availabilityId) {
        return doctorAvailabilityService.deleteAvailability(availabilityId);
    }

    // Count available doctors on a specific date
    @GetMapping("/available/count")
    public ResponseEntity<AvailableDoctorsCountResponse> countAvailableDoctors(
            @RequestParam("date") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {
        AvailableDoctorsCountResponse response = doctorAvailabilityService.countAvailableDoctorsOn(date);
        return ResponseEntity.ok(response);
    }
}