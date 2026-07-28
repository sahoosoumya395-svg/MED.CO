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

    // Create Doctor Availability
    @PostMapping("/create")
    public ApiResponse<?> addAvailability(@RequestBody DoctorAvailabilityRequest request) {
        return doctorAvailabilityService.addAvailability(request);
    }

    // Get Availability By Doctor Id
    @GetMapping("/get/{doctorId}")
    public ApiResponse<?> getAvailabilityByDoctor(@PathVariable Long doctorId) {
        return doctorAvailabilityService.getAvailabilityByDoctor(doctorId);
    }

    // Update Availability
    @PutMapping("/update/{availabilityId}")
    public ApiResponse<?> updateAvailability(
            @PathVariable Long availabilityId,
            @RequestBody DoctorAvailabilityRequest request) {

        return doctorAvailabilityService.updateAvailability(availabilityId, request);
    }

    // Delete Availability
    @DeleteMapping("/delete/{availabilityId}")
    public ApiResponse<?> deleteAvailability(@PathVariable Long availabilityId) {
        return doctorAvailabilityService.deleteAvailability(availabilityId);
    }

    // Count Available Doctors
    @GetMapping("/available/count")
    public ResponseEntity<AvailableDoctorsCountResponse> countAvailableDoctors(
            @RequestParam("date")
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
            LocalDate date) {

        AvailableDoctorsCountResponse response =
                doctorAvailabilityService.countAvailableDoctorsOn(date);

        return ResponseEntity.ok(response);
    }
}