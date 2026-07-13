package com.med.co.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.med.co.dto.request.DoctorRegistrationRequest;
import com.med.co.dto.response.DoctorResponseDto;
import com.med.co.service.DoctorService;

@RestController
@RequestMapping("/api/doctors")
@CrossOrigin("*")
public class DoctorController {

    @Autowired
    private DoctorService doctorService;

    @PostMapping("/register")
    public ResponseEntity<DoctorResponseDto> registerDoctor(
            @RequestBody DoctorRegistrationRequest request) {

        DoctorResponseDto response = doctorService.registerDoctor(request);

        return ResponseEntity.ok(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<DoctorResponseDto> getDoctorById(
            @PathVariable Long id) {

        DoctorResponseDto response = doctorService.getDoctorById(id);

        return ResponseEntity.ok(response);
    }
}