package com.med.co.service;

import org.springframework.data.domain.Page;

//import java.util.List;

import com.med.co.dto.request.DoctorRegistrationRequest;
import com.med.co.dto.request.LeaveStatusRequestDto;
import com.med.co.dto.response.DoctorLeaveResponseDto;
import com.med.co.dto.response.DoctorResponseDto;

public interface DoctorService {

    DoctorResponseDto registerDoctor(DoctorRegistrationRequest request);

    DoctorResponseDto getDoctorById(Long id);

//    List<DoctorResponseDto> getAllDoctors();
    Page<DoctorResponseDto> getAllDoctors(
            int page,
            int size,
            String sortBy,
            String direction);

    DoctorResponseDto updateDoctor(Long id, DoctorRegistrationRequest request);

    String deleteDoctor(Long id);
<<<<<<< HEAD
    DoctorLeaveResponseDto updateLeaveStatus(Long leaveId,
            LeaveStatusRequestDto request);
=======
    long getTotalDoctors();
>>>>>>> f0115cec8e81bf178ccbb127e55ee5d1a98b9a52
}