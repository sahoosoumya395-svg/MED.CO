package com.med.co.service;

import com.med.co.dto.request.DoctorRegistrationRequest;
import com.med.co.dto.response.DoctorResponseDto;

public interface DoctorService {

    DoctorResponseDto registerDoctor(DoctorRegistrationRequest request);

    DoctorResponseDto getDoctorById(Long id);

}