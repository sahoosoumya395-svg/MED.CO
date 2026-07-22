package com.med.co.serviceimpl;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.med.co.dto.request.DoctorLeaveRequestDto;
import com.med.co.dto.request.LeaveStatusRequestDto;
import com.med.co.dto.response.DoctorLeaveResponseDto;
import com.med.co.entity.Doctor;
import com.med.co.entity.DoctorLeave;
import com.med.co.repository.DoctorLeaveRepository;
import com.med.co.repository.DoctorRepository;
import com.med.co.service.DoctorLeaveService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class DoctorLeaveServiceImpl implements DoctorLeaveService {

    private final DoctorLeaveRepository doctorLeaveRepository;
    private final DoctorRepository doctorRepository;

    @Override
    public DoctorLeaveResponseDto applyLeave(DoctorLeaveRequestDto request) {

        Doctor doctor = doctorRepository.findById(request.getDoctorId())
                .orElseThrow(() -> new RuntimeException("Doctor not found"));

        DoctorLeave leave = DoctorLeave.builder()
                .doctor(doctor)
                .leaveType(request.getLeaveType())
                .fromDate(request.getFromDate())
                .toDate(request.getToDate())
                .reason(request.getReason())
                .build();

        DoctorLeave saved = doctorLeaveRepository.save(leave);

        return mapToResponse(saved);
    }

    @Override
    public DoctorLeaveResponseDto getLeaveById(Long leaveId) {

        DoctorLeave leave = doctorLeaveRepository.findById(leaveId)
                .orElseThrow(() -> new RuntimeException("Leave not found"));

        return mapToResponse(leave);
    }

    @Override
    public List<DoctorLeaveResponseDto> getAllLeaves() {

        return doctorLeaveRepository.findAll()
                .stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    @Override
    public List<DoctorLeaveResponseDto> getLeavesByDoctor(Long doctorId) {

        return doctorLeaveRepository.findByDoctorId(doctorId)
                .stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    @Override
    public DoctorLeaveResponseDto updateLeave(Long leaveId,
                                           DoctorLeaveRequestDto request) {

        DoctorLeave leave = doctorLeaveRepository.findById(leaveId)
                .orElseThrow(() -> new RuntimeException("Leave not found"));

        leave.setLeaveType(request.getLeaveType());
        leave.setFromDate(request.getFromDate());
        leave.setToDate(request.getToDate());
        leave.setReason(request.getReason());

        DoctorLeave updated = doctorLeaveRepository.save(leave);

        return mapToResponse(updated);
    }

    @Override
    public void deleteLeave(Long leaveId) {

        doctorLeaveRepository.deleteById(leaveId);
    }

    private DoctorLeaveResponseDto mapToResponse(DoctorLeave leave) {

        return DoctorLeaveResponseDto.builder()
                .leaveId(leave.getLeaveId())
                .doctorId(leave.getDoctor().getId())
                .doctorName(
                        leave.getDoctor().getFirstName() + " "
                        + leave.getDoctor().getLastName())
                .leaveType(leave.getLeaveType())
                .fromDate(leave.getFromDate())
                .toDate(leave.getToDate())
                .reason(leave.getReason())
                .status(leave.getStatus())
                .build();
    }
    @Override
    public DoctorLeaveResponseDto updateLeaveStatus(Long leaveId,
                                                 LeaveStatusRequestDto request) {

        DoctorLeave leave = doctorLeaveRepository.findById(leaveId)
                .orElseThrow(() -> new RuntimeException("Leave not found"));

        leave.setStatus(request.getStatus());

        DoctorLeave updated = doctorLeaveRepository.save(leave);

        return mapToResponse(updated);
    }

}