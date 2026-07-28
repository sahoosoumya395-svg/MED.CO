package com.med.co.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;

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

    @PostMapping("/apply")
    public ResponseEntity<DoctorLeaveResponseDto> applyLeave(
            @Valid @RequestBody DoctorLeaveRequestDto request) {

        return new ResponseEntity<>(
                doctorLeaveService.applyLeave(request),
                HttpStatus.CREATED);
    }

    @GetMapping("/view/{leaveId}")
    public ResponseEntity<DoctorLeaveResponseDto> getLeaveById(
            @PathVariable Long leaveId) {

        return ResponseEntity.ok(
                doctorLeaveService.getLeaveById(leaveId));
    }

    @GetMapping("/view/all")
    public ResponseEntity<List<DoctorLeaveResponseDto>> getAllLeaves() {

        return ResponseEntity.ok(
                doctorLeaveService.getAllLeaves());
    }

    @GetMapping("/view/{doctorId}")
    public ResponseEntity<List<DoctorLeaveResponseDto>> getLeavesByDoctor(
            @PathVariable Long doctorId) {

        return ResponseEntity.ok(
                doctorLeaveService.getLeavesByDoctor(doctorId));
    }

    @PutMapping("/own-update/{leaveId}")
    public ResponseEntity<DoctorLeaveResponseDto> updateLeave(
            @PathVariable Long leaveId,
            @Valid @RequestBody DoctorLeaveRequestDto request) {

        return ResponseEntity.ok(
                doctorLeaveService.updateLeave(leaveId, request));
    }
    
    @PutMapping("/admin/{leaveId}/status")
    public ResponseEntity<DoctorLeaveResponseDto> updateLeaveStatus(
            @PathVariable Long leaveId,
            @RequestBody @Valid LeaveStatusRequestDto request) {

        return ResponseEntity.ok(
                doctorLeaveService.updateLeaveStatus(leaveId, request));
    }

    @DeleteMapping("/delete/{leaveId}")
    public ResponseEntity<String> deleteLeave(
            @PathVariable Long leaveId) {

        doctorLeaveService.deleteLeave(leaveId);

        return ResponseEntity.ok("Doctor Leave deleted successfully.");
    }
    
    @GetMapping("/all-leave/count/date-wise")
    public ResponseEntity<Long> countDoctorsOnLeave(
            @RequestParam(required = false)
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {

        if (date == null) {
            date = LocalDate.now();
        }

        return ResponseEntity.ok(
                doctorLeaveService.countDoctorsOnLeave(date));
    }

    @GetMapping("/count/total")
    public ResponseEntity<Long> countTotalLeaves() {
        return ResponseEntity.ok(
                doctorLeaveService.countTotalLeaves());
    }

    @GetMapping("/count/status-wise")
    public ResponseEntity<Long> countLeavesByStatus(
            @PathVariable String status) {

        try {
            com.med.co.enums.LeaveStatus leaveStatus = 
                    com.med.co.enums.LeaveStatus.valueOf(status.toUpperCase());
            return ResponseEntity.ok(
                    doctorLeaveService.countLeavesByStatus(leaveStatus));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @GetMapping("/count/active-leaves")
    public ResponseEntity<Long> countActiveLeaves() {
        return ResponseEntity.ok(
                doctorLeaveService.countActiveLeaves());
    }
    
    
    

}