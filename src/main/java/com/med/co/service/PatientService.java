package com.med.co.service;

import com.med.co.dto.request.PatientRegistrationRequest;
import com.med.co.dto.response.ApiResponse;

public interface PatientService {

    ApiResponse<?> registerPatient(PatientRegistrationRequest request);

    ApiResponse<?> getAllPatients();

    ApiResponse<?> getPatientById(Long patientId);

}