package com.med.co.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.med.co.dto.request.DoctorRegistrationRequest;
import com.med.co.dto.request.DoctorUpdateRequest;
import com.med.co.dto.request.LeaveStatusRequestDto;
import com.med.co.dto.response.DoctorLeaveResponseDto;
import com.med.co.dto.response.DoctorResponseDto;
import com.med.co.service.DoctorService;

@RestController
@RequestMapping("/api/doctors")
@CrossOrigin(origins = "*")
public class DoctorController {

    @Autowired
    private DoctorService doctorService;

    // Register Doctor
    @PostMapping("/register")
    public ResponseEntity<DoctorResponseDto> registerDoctor(
            @RequestBody DoctorRegistrationRequest request) {

        return ResponseEntity.ok(doctorService.registerDoctor(request));
    }

    // Get All Doctors with Pagination and Sorting
    @GetMapping("/view/all")
    // Get All Doctors with Pagination & Sorting
    @GetMapping
    public ResponseEntity<Page<DoctorResponseDto>> getAllDoctors(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "doctorId") String sortBy,
            @RequestParam(defaultValue = "asc") String direction) {

        return ResponseEntity.ok(
                doctorService.getAllDoctors(page, size, sortBy, direction));
    }

    // Get Doctor By Id
    @GetMapping("/view/{id}")
    // Get Doctor By ID
    @GetMapping("/{id}")
    public ResponseEntity<DoctorResponseDto> getDoctorById(@PathVariable Long id) {

        return ResponseEntity.ok(doctorService.getDoctorById(id));
    }

    // Update Doctor
    @PutMapping("/update/{id}")
    public ResponseEntity<DoctorResponseDto> updateDoctor(
            @PathVariable Long id,
            @RequestBody DoctorUpdateRequest request) {

        return ResponseEntity.ok(
                doctorService.updateDoctor(id, request));
    }

    // Delete Doctor
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<String> deleteDoctor(@PathVariable Long id) {

        return ResponseEntity.ok(
                doctorService.deleteDoctor(id));
    }

    // Update Leave Status
    @PutMapping("/leave/{leaveId}/status")
    public ResponseEntity<DoctorLeaveResponseDto> updateLeaveStatus(
            @PathVariable Long leaveId,
            @RequestBody LeaveStatusRequestDto request) {

        return ResponseEntity.ok(
                doctorService.updateLeaveStatus(leaveId, request));
    }

    // Count Total Doctors
    @GetMapping("/count")
    public ResponseEntity<Long> getTotalDoctors() {

        return ResponseEntity.ok(
                doctorService.getTotalDoctors());
    }

    // Get Doctors By Department
    @GetMapping("/department/{departmentId}")
    public ResponseEntity<List<DoctorResponseDto>> getDoctorsByDepartment(
            @PathVariable Long departmentId) {

        return ResponseEntity.ok(
                doctorService.getDoctorsByDepartment(departmentId));
    }
}