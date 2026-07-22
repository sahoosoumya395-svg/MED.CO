package com.med.co.serviceimpl;

import java.util.List;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.med.co.dto.request.DoctorAvailabilityRequest;
import com.med.co.dto.response.ApiResponse;
import com.med.co.entity.Doctor;
import com.med.co.entity.DoctorAvailability;
import com.med.co.repository.DoctorAvailabilityRepository;
import com.med.co.repository.DoctorRepository;
import com.med.co.service.DoctorAvailabilityService;

@Service
public class DoctorAvailabilityServiceImpl implements DoctorAvailabilityService {

    @Autowired
    private DoctorAvailabilityRepository availabilityRepository;

    @Autowired
    private DoctorRepository doctorRepository;

    @Autowired
    private ModelMapper modelMapper;

    @Override
    public ApiResponse<?> addAvailability(DoctorAvailabilityRequest request) {

        Doctor doctor = doctorRepository.findById(request.getDoctorId())
                .orElseThrow(() -> new RuntimeException("Doctor not found"));

        DoctorAvailability availability = new DoctorAvailability();

        availability.setDoctor(doctor);
        availability.setDayOfWeek(request.getDayOfWeek());
        availability.setStartTime(request.getStartTime());
        availability.setEndTime(request.getEndTime());
        availability.setAvailable(request.getAvailable());

        availabilityRepository.save(availability);

        return new ApiResponse<>(201, "Doctor availability added successfully", availability);
    }

    @Override
    public ApiResponse<?> getAvailabilityByDoctor(Long doctorId) {

        List<DoctorAvailability> list = availabilityRepository.findByDoctorId(doctorId);

        return new ApiResponse<>(200, "Doctor availability fetched successfully", list);
    }

    @Override
    public ApiResponse<?> updateAvailability(Long availabilityId,
            DoctorAvailabilityRequest request) {

        DoctorAvailability availability = availabilityRepository.findById(availabilityId)
                .orElseThrow(() -> new RuntimeException("Availability not found"));

        availability.setDayOfWeek(request.getDayOfWeek());
        availability.setStartTime(request.getStartTime());
        availability.setEndTime(request.getEndTime());
        availability.setAvailable(request.getAvailable());

        availabilityRepository.save(availability);

        return new ApiResponse<>(200, "Doctor availability updated successfully", availability);
    }

    @Override
    public ApiResponse<?> deleteAvailability(Long availabilityId) {

        DoctorAvailability availability = availabilityRepository.findById(availabilityId)
                .orElseThrow(() -> new RuntimeException("Availability not found"));

        availabilityRepository.delete(availability);

        return new ApiResponse<>(200, "Doctor availability deleted successfully", null);
    }
}