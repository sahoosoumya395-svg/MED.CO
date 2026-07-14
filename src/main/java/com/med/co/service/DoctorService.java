package com.med.co.service;

import java.util.List;

import com.med.co.dto.request.DoctorRegistrationRequest;
import com.med.co.dto.response.DoctorResponseDto;

public interface DoctorService {

    DoctorResponseDto registerDoctor(DoctorRegistrationRequest request);

    DoctorResponseDto getDoctorById(Long id);

    List<DoctorResponseDto> getAllDoctors();

    DoctorResponseDto updateDoctor(Long id, DoctorRegistrationRequest request);

    String deleteDoctor(Long id);
}