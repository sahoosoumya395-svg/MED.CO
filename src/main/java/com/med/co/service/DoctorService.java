package com.med.co.service;

import org.springframework.data.domain.Page;

import com.med.co.dto.request.DoctorRegistrationRequest;
import com.med.co.dto.request.DoctorUpdateRequest;
import com.med.co.dto.request.LeaveStatusRequestDto;
import com.med.co.dto.response.DoctorLeaveResponseDto;
import com.med.co.dto.response.DoctorResponseDto;

public interface DoctorService {

	DoctorResponseDto registerDoctor(DoctorRegistrationRequest request);

	DoctorResponseDto getDoctorById(Long id);

	Page<DoctorResponseDto> getAllDoctors(int page, int size, String sortBy, String direction);

	DoctorResponseDto updateDoctor(Long id, DoctorUpdateRequest request);

	String deleteDoctor(Long id);

	DoctorLeaveResponseDto updateLeaveStatus(Long leaveId, LeaveStatusRequestDto request);

	long getTotalDoctors();
}