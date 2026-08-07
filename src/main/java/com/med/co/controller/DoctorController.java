package com.med.co.controller;

import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.med.co.dto.request.DoctorRegistrationRequest;
import com.med.co.dto.request.DoctorUpdateRequest;
import com.med.co.dto.response.DoctorResponseDto;
import com.med.co.service.DoctorService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/doctors")
@CrossOrigin("*")
public class DoctorController {

    private final DoctorService doctorService;

    public DoctorController(DoctorService doctorService) {
        this.doctorService = doctorService;
    }

    // Register Doctor
    @PostMapping("/register")
    public ResponseEntity<DoctorResponseDto> registerDoctor(
            @Valid @RequestBody DoctorRegistrationRequest request) {

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(doctorService.registerDoctor(request));
    }

    // Get All Doctors with Pagination and Sorting
    @GetMapping("/view/all")
    public ResponseEntity<Page<DoctorResponseDto>> getAllDoctors(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "5") int size,
            @RequestParam(defaultValue = "firstName") String sortBy,
            @RequestParam(defaultValue = "asc") String direction) {

        return ResponseEntity.ok(
                doctorService.getAllDoctors(page, size, sortBy, direction));
    }

    // Get Doctor By Id
    @GetMapping("/view/{id}")
    public ResponseEntity<DoctorResponseDto> getDoctorById(@PathVariable Long id) {

        return ResponseEntity.ok(doctorService.getDoctorById(id));
    }

    // Update Doctor
    @PutMapping("/update/{id}")
    public ResponseEntity<DoctorResponseDto> updateDoctor(
            @PathVariable Long id,
            @Valid @RequestBody DoctorUpdateRequest request) {

        return ResponseEntity.ok(doctorService.updateDoctor(id, request));
    }

    // Delete Doctor
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<String> deleteDoctor(@PathVariable Long id) {

        return ResponseEntity.ok(doctorService.deleteDoctor(id));
    }

    // Count Total Doctors
    @GetMapping("/count")
    public ResponseEntity<Long> getTotalDoctors() {

        return ResponseEntity.ok(doctorService.getTotalDoctors());
    }
}