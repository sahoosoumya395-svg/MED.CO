package com.med.co.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.med.co.dto.request.DoctorLeaveRequestDto;
import com.med.co.dto.request.LeaveStatusRequestDto;
import com.med.co.dto.response.DoctorLeaveResponseDto;
import com.med.co.service.DoctorLeaveService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/doctor-leaves")
@RequiredArgsConstructor
public class DoctorLeaveController {

    private final DoctorLeaveService doctorLeaveService;

    @PostMapping
    public ResponseEntity<DoctorLeaveResponseDto> applyLeave(
            @Valid @RequestBody DoctorLeaveRequestDto request) {

        return new ResponseEntity<>(
                doctorLeaveService.applyLeave(request),
                HttpStatus.CREATED);
    }

    @GetMapping("/{leaveId}")
    public ResponseEntity<DoctorLeaveResponseDto> getLeaveById(
            @PathVariable Long leaveId) {

        return ResponseEntity.ok(
                doctorLeaveService.getLeaveById(leaveId));
    }

    @GetMapping
    public ResponseEntity<List<DoctorLeaveResponseDto>> getAllLeaves() {

        return ResponseEntity.ok(
                doctorLeaveService.getAllLeaves());
    }

    @GetMapping("/doctor/{doctorId}")
    public ResponseEntity<List<DoctorLeaveResponseDto>> getLeavesByDoctor(
            @PathVariable Long doctorId) {

        return ResponseEntity.ok(
                doctorLeaveService.getLeavesByDoctor(doctorId));
    }

    @PutMapping("/{leaveId}")
    public ResponseEntity<DoctorLeaveResponseDto> updateLeave(
            @PathVariable Long leaveId,
            @Valid @RequestBody DoctorLeaveRequestDto request) {

        return ResponseEntity.ok(
                doctorLeaveService.updateLeave(leaveId, request));
    }
    
    @PutMapping("/{leaveId}/status")
    public ResponseEntity<DoctorLeaveResponseDto> updateLeaveStatus(
            @PathVariable Long leaveId,
            @RequestBody @Valid LeaveStatusRequestDto request) {

        return ResponseEntity.ok(
                doctorLeaveService.updateLeaveStatus(leaveId, request));
    }

    @DeleteMapping("/{leaveId}")
    public ResponseEntity<String> deleteLeave(
            @PathVariable Long leaveId) {

        doctorLeaveService.deleteLeave(leaveId);

        return ResponseEntity.ok("Doctor Leave deleted successfully.");
    }
    
    
    

}