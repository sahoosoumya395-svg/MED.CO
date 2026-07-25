package com.med.co.controller;

//import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.med.co.dto.request.DoctorRegistrationRequest;
import com.med.co.dto.response.ApiResponse;
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
    public ResponseEntity<ApiResponse<DoctorResponseDto>> registerDoctor(
            @RequestBody DoctorRegistrationRequest request) {

        DoctorResponseDto doctor = doctorService.registerDoctor(request);

        ApiResponse<DoctorResponseDto> response = new ApiResponse<>();
        response.setStatusCode(200);
        response.setMessage("Doctor registered successfully");
        response.setData(doctor);

        return ResponseEntity.ok(response);
    }
    @GetMapping("/all")
    public ResponseEntity<Page<DoctorResponseDto>> getAllDoctors(

            @RequestParam(defaultValue = "0") int page,

            @RequestParam(defaultValue = "5") int size,

            @RequestParam(defaultValue = "firstName") String sortBy,

            @RequestParam(defaultValue = "asc") String direction) {

        return ResponseEntity.ok(
                doctorService.getAllDoctors(
                        page,
                        size,
                        sortBy,
                        direction));
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
    
    @GetMapping("/count")
    public ResponseEntity<Long> getTotalDoctors() {

        return ResponseEntity.ok(
                doctorService.getTotalDoctors());

    }
}