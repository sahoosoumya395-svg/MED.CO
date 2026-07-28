package com.med.co.service;

import com.med.co.dto.response.ApiResponse;
import com.med.co.entity.Appointment;

public interface MasterService {

    // Automatically called from AppointmentServiceImpl
    ApiResponse<?> createMasterRecord(Appointment appointment);

    // Get all master records
    ApiResponse<?> getAllMasters();

    // Get master record by ID
    ApiResponse<?> getMasterById(Long masterId);

    // Get all records by MRN
    ApiResponse<?> getByMrn(String mrnNo);

    // Delete master record
    ApiResponse<?> deleteMaster(Long masterId);

}