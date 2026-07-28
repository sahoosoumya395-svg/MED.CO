package com.med.co.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.med.co.dto.request.PrescriptionRequestDto;
import com.med.co.dto.response.PrescriptionResponseDto;
import com.med.co.service.PrescriptionService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/prescriptions")
@RequiredArgsConstructor
public class PrescriptionController {

    private final PrescriptionService prescriptionService;

    // Create Prescription
    @PostMapping("/create")
    public ResponseEntity<PrescriptionResponseDto> createPrescription(
            @RequestBody PrescriptionRequestDto requestDto) {

        PrescriptionResponseDto response =
                prescriptionService.createPrescription(requestDto);

        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }


    // Get Prescription By Id
    @GetMapping("/get/{prescriptionId}")

  

    public ResponseEntity<PrescriptionResponseDto> getPrescriptionById(
            @PathVariable Long prescriptionId) {

        PrescriptionResponseDto response =
                prescriptionService.getPrescriptionById(prescriptionId);

        return ResponseEntity.ok(response);
    }


    // Get Prescription By Appointment
    @GetMapping("/appointment/get/{appointmentId}")



    public ResponseEntity<PrescriptionResponseDto> getPrescriptionByAppointment(
            @PathVariable Long appointmentId) {

        PrescriptionResponseDto response =
                prescriptionService.getPrescriptionByAppointment(appointmentId);

        return ResponseEntity.ok(response);
    }


    // Get Prescriptions By Patient
  

    // NEW API - Fetch prescription using MRN No
    @GetMapping("/mrn/{mrnNo}")
    public ResponseEntity<PrescriptionResponseDto> getPrescriptionByMrnNo(
            @PathVariable String mrnNo) {

        PrescriptionResponseDto response =
                prescriptionService.getPrescriptionByMrnNo(mrnNo);

        return ResponseEntity.ok(response);
    }

    @GetMapping("/patient/{patientId}")

    public ResponseEntity<List<PrescriptionResponseDto>> getPrescriptionsByPatient(
            @PathVariable Long patientId) {

        List<PrescriptionResponseDto> response =
                prescriptionService.getPrescriptionsByPatient(patientId);

        return ResponseEntity.ok(response);
    }


    // Get Prescriptions By Doctor
    @GetMapping("/doctor/get/{doctorId}")

    

    public ResponseEntity<List<PrescriptionResponseDto>> getPrescriptionsByDoctor(
            @PathVariable Long doctorId) {

        List<PrescriptionResponseDto> response =
                prescriptionService.getPrescriptionsByDoctor(doctorId);

        return ResponseEntity.ok(response);
    }


    // Get All Prescriptions
    @GetMapping("/getAll")

    

    public ResponseEntity<List<PrescriptionResponseDto>> getAllPrescriptions() {

        List<PrescriptionResponseDto> response =
                prescriptionService.getAllPrescriptions();

        return ResponseEntity.ok(response);
    }
}