package com.med.co.service;

import java.time.LocalDate;

import com.med.co.dto.request.DoctorAvailabilityRequest;
import com.med.co.dto.response.ApiResponse;
import com.med.co.dto.response.AvailableDoctorsCountResponse;

public interface DoctorAvailabilityService {

    // Add doctor availability
    ApiResponse<?> addAvailability(DoctorAvailabilityRequest request);

    // Get all availability of a doctor
    ApiResponse<?> getAvailabilityByDoctor(Long doctorId);

    // Update availability
    ApiResponse<?> updateAvailability(Long availabilityId, DoctorAvailabilityRequest request);

    // Delete availability
    ApiResponse<?> deleteAvailability(Long availabilityId);

    // Count available doctors on a specific date
    AvailableDoctorsCountResponse countAvailableDoctorsOn(LocalDate date);
}