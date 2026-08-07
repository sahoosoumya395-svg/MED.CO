package com.med.co.service;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

import com.med.co.dto.request.AppointmentRequestDto;
import com.med.co.dto.response.AppointmentResponseDto;

public interface AppointmentService {

    AppointmentResponseDto bookAppointment(AppointmentRequestDto requestDto);

    AppointmentResponseDto cancelAppointment(Long appointmentId);

    AppointmentResponseDto getAppointmentById(Long appointmentId);

    List<AppointmentResponseDto> getAppointmentsByDoctor(Long doctorId);

    List<AppointmentResponseDto> getAppointmentsByPatient(Long patientId);

    List<LocalDate> getAvailableDates(Long doctorId);

    List<LocalTime> getAvailableTimeSlots(Long doctorId, LocalDate appointmentDate);

    long countAppointmentsToday();

}