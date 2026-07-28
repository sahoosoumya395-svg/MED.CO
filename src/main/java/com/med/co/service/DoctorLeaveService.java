package com.med.co.service;

import java.util.List;
import java.time.LocalDate;

import com.med.co.dto.request.DoctorLeaveRequestDto;
import com.med.co.dto.request.LeaveStatusRequestDto;
import com.med.co.dto.response.DoctorLeaveResponseDto;
import com.med.co.enums.LeaveStatus;

public interface DoctorLeaveService {

    DoctorLeaveResponseDto applyLeave(DoctorLeaveRequestDto request);

    DoctorLeaveResponseDto getLeaveById(Long leaveId);

    List<DoctorLeaveResponseDto> getAllLeaves();

    List<DoctorLeaveResponseDto> getLeavesByDoctor(Long doctorId);

    DoctorLeaveResponseDto updateLeave(Long leaveId, DoctorLeaveRequestDto request);

    DoctorLeaveResponseDto updateLeaveStatus(Long leaveId,
                                             LeaveStatusRequestDto request);

    void deleteLeave(Long leaveId);

    // Count number of distinct doctors on leave for a given date
    long countDoctorsOnLeave(LocalDate date);

    // Count total number of leaves
    long countTotalLeaves();

    // Count leaves by status
    long countLeavesByStatus(LeaveStatus status);

    // Count currently active leaves (approved leaves that are ongoing)
    long countActiveLeaves();

}