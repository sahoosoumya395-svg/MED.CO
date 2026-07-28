package com.med.co.service;

import java.util.List;

import com.med.co.dto.request.PrescriptionRequestDto;
import com.med.co.dto.response.PrescriptionResponseDto;

public interface PrescriptionService {

    /**
     * Create a new prescription
     */
    PrescriptionResponseDto createPrescription(PrescriptionRequestDto requestDto);

    /**
     * Get prescription by Prescription ID
     */
    PrescriptionResponseDto getPrescriptionById(Long prescriptionId);

    /**
     * Get prescription by Appointment ID
     */
    PrescriptionResponseDto getPrescriptionByAppointment(Long appointmentId);

    /**
     * Get all prescriptions of a Patient
     */
    List<PrescriptionResponseDto> getPrescriptionsByPatient(Long patientId);

    /**
     * Get all prescriptions written by a Doctor
     */
    List<PrescriptionResponseDto> getPrescriptionsByDoctor(Long doctorId);

    /**
     * Get all prescriptions
     */
    List<PrescriptionResponseDto> getAllPrescriptions();

}