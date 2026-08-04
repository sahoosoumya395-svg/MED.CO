package com.med.co.controller;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import jakarta.validation.Valid;

import com.med.co.dto.request.AppointmentRequestDto;
import com.med.co.dto.response.AppointmentResponseDto;
import com.med.co.service.AppointmentService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/appointments")
@CrossOrigin(origins = "http://localhost:4200")
@RequiredArgsConstructor
public class AppointmentController {

    private final AppointmentService appointmentService;

    /**
     * Book Appointment
     */
    @PostMapping("/book")
    public ResponseEntity<AppointmentResponseDto> bookAppointment(
            @Valid @RequestBody AppointmentRequestDto requestDto) {

        AppointmentResponseDto response =
                appointmentService.bookAppointment(requestDto);

        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    /**
     * Cancel Appointment
     */
    @PutMapping("/cancel/{appointmentId}")
    public ResponseEntity<AppointmentResponseDto> cancelAppointment(
            @PathVariable Long appointmentId) {

        AppointmentResponseDto response =
                appointmentService.cancelAppointment(appointmentId);

        return ResponseEntity.ok(response);
    }

    /**
     * Get Appointment By Id
     */
    @GetMapping("/get/{appointmentId}")
    public ResponseEntity<AppointmentResponseDto> getAppointmentById(
            @PathVariable Long appointmentId) {

        AppointmentResponseDto response =
                appointmentService.getAppointmentById(appointmentId);

        return ResponseEntity.ok(response);
    }

    /**
     * Get All Appointments Of Doctor
     */
    @GetMapping("/doctor/get/{doctorId}")
    public ResponseEntity<List<AppointmentResponseDto>> getAppointmentsByDoctor(
            @PathVariable Long doctorId) {

        List<AppointmentResponseDto> response =
                appointmentService.getAppointmentsByDoctor(doctorId);

        return ResponseEntity.ok(response);
    }

    /**
     * Get All Appointments Of Patient
     */
    @GetMapping("/patient/get/{patientId}")
    public ResponseEntity<List<AppointmentResponseDto>> getAppointmentsByPatient(
            @PathVariable Long patientId) {

        List<AppointmentResponseDto> response =
                appointmentService.getAppointmentsByPatient(patientId);

        return ResponseEntity.ok(response);
    }

    /**
     * Get Available Dates For Doctor
     */
    @GetMapping("/available-dates/get/{doctorId}")
    public ResponseEntity<List<LocalDate>> getAvailableDates(
            @PathVariable Long doctorId) {

        List<LocalDate> response =
                appointmentService.getAvailableDates(doctorId);

        return ResponseEntity.ok(response);
    }

    /**
     * Get Available Time Slots
     */
    @GetMapping("/available-slots/get")
    public ResponseEntity<List<LocalTime>> getAvailableTimeSlots(
            @RequestParam Long doctorId,
            @RequestParam LocalDate appointmentDate) {

        List<LocalTime> response =
                appointmentService.getAvailableTimeSlots(
                        doctorId,
                        appointmentDate);

        return ResponseEntity.ok(response);
    }

    /**
     * Count All Appointments Today
     */
    @GetMapping("/count/today")
    public ResponseEntity<Long> countAppointmentsToday() {
        long count = appointmentService.countAppointmentsToday();
        return ResponseEntity.ok(count);
    }

}