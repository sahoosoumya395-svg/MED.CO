package com.med.co.serviceimpl;

import org.springframework.stereotype.Service;

import com.med.co.dto.response.ApiResponse;
import com.med.co.entity.Appointment;
import com.med.co.entity.Master;
import com.med.co.repository.MasterRepository;
import com.med.co.service.MasterService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class MasterServiceImpl implements MasterService {

    private final MasterRepository masterRepository;

    @Override
    public ApiResponse<?> createMasterRecord(Appointment appointment) {

        Master master = Master.builder()
                .mrnNo(appointment.getPatient().getMrnNo())
                .patient(appointment.getPatient())
                .doctor(appointment.getDoctor())
                .department(appointment.getDoctor().getDepartment())
                .appointment(appointment)
                .appointmentDate(appointment.getAppointmentDate())
                .appointmentTime(appointment.getAppointmentTime())
                .status(appointment.getStatus().name())
                .build();

        masterRepository.save(master);

        return new ApiResponse<>(200, "Master record created successfully", master);
    }
    
    
    @Override
    public ApiResponse<?> getAllMasters() {
        return new ApiResponse<>(200, "Success", masterRepository.findAll());
    }

    @Override
    public ApiResponse<?> getMasterById(Long masterId) {
        Master master = masterRepository.findById(masterId)
                .orElseThrow(() -> new RuntimeException("Master record not found"));

        return new ApiResponse<>(200, "Success", master);
    }

    @Override
    public ApiResponse<?> getByMrn(String mrnNo) {
        return new ApiResponse<>(200, "Success",
                masterRepository.findByMrnNo(mrnNo));
    }

    @Override
    public ApiResponse<?> deleteMaster(Long masterId) {

        Master master = masterRepository.findById(masterId)
                .orElseThrow(() -> new RuntimeException("Master record not found"));

        masterRepository.delete(master);

        return new ApiResponse<>(200, "Master record deleted successfully", null);
    }
}