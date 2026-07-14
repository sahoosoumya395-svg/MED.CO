package com.med.co.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.med.co.dto.request.DoctorRegistrationRequest;
import com.med.co.dto.response.DoctorResponseDto;
import com.med.co.service.DoctorService;

@RestController
@RequestMapping("/api/doctors")
@CrossOrigin("*")
public class DoctorController {

    private final DoctorService doctorService;

    DoctorController(DoctorService doctorService) {
        this.doctorService = doctorService;
    }

    @PostMapping("/register")
    public ResponseEntity<DoctorResponseDto> registerDoctor(
            @RequestBody DoctorRegistrationRequest request) {

        return ResponseEntity.ok(
                doctorService.registerDoctor(request));
    }

    @GetMapping("/{id}")
    public ResponseEntity<DoctorResponseDto> getDoctorById(
            @PathVariable Long id) {

        return ResponseEntity.ok(
                doctorService.getDoctorById(id));
    }

    @GetMapping("/all")
    public ResponseEntity<List<DoctorResponseDto>> getAllDoctors() {

        return ResponseEntity.ok(
                doctorService.getAllDoctors());
    }

    @PutMapping("/{id}")
    public ResponseEntity<DoctorResponseDto> updateDoctor(
            @PathVariable Long id,
            @RequestBody DoctorRegistrationRequest request) {

        return ResponseEntity.ok(
                doctorService.updateDoctor(id, request));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteDoctor(
            @PathVariable Long id) {

        return ResponseEntity.ok(
                doctorService.deleteDoctor(id));
    }
}