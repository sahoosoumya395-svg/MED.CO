package com.med.co.service;

import java.util.List;

import com.med.co.dto.request.DoctorLeaveRequestDto;
import com.med.co.dto.request.LeaveStatusRequestDto;
import com.med.co.dto.response.DoctorLeaveResponseDto;

public interface DoctorLeaveService {

    DoctorLeaveResponseDto applyLeave(DoctorLeaveRequestDto request);

    DoctorLeaveResponseDto getLeaveById(Long leaveId);

    List<DoctorLeaveResponseDto> getAllLeaves();

    List<DoctorLeaveResponseDto> getLeavesByDoctor(Long doctorId);

    DoctorLeaveResponseDto updateLeave(Long leaveId, DoctorLeaveRequestDto request);

    DoctorLeaveResponseDto updateLeaveStatus(Long leaveId,
                                             LeaveStatusRequestDto request);

    void deleteLeave(Long leaveId);
}