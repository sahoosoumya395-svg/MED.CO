package com.med.co.serviceimpl;

import java.util.List;

import org.springframework.stereotype.Service;

import com.med.co.dto.request.PrescriptionRequestDto;
import com.med.co.dto.response.PrescriptionResponseDto;
import com.med.co.entity.Appointment;
import com.med.co.entity.Prescription;
import com.med.co.repository.AppointmentRepository;
import com.med.co.repository.PrescriptionRepository;
import com.med.co.service.PrescriptionService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class PrescriptionServiceImpl implements PrescriptionService {

    private final PrescriptionRepository prescriptionRepository;
    private final AppointmentRepository appointmentRepository;

    @Override
    public PrescriptionResponseDto createPrescription(PrescriptionRequestDto requestDto) {

        // Check Appointment
        Appointment appointment = appointmentRepository.findById(requestDto.getAppointmentId())
                .orElseThrow(() ->
                        new RuntimeException("Appointment not found"));

        // Check if Prescription already exists
        if (prescriptionRepository.existsByAppointment(appointment)) {
            throw new RuntimeException("Prescription already exists for this appointment");
        }

        // Create Prescription
        Prescription prescription = Prescription.builder()
                .appointment(appointment)
                .doctor(appointment.getDoctor())
                .patient(appointment.getPatient())
                .diagnosis(requestDto.getDiagnosis())
                .medicines(requestDto.getMedicines())
                .advice(requestDto.getAdvice())
        
                .build();

        Prescription savedPrescription = prescriptionRepository.save(prescription);

        return mapToResponse(savedPrescription);
    }

    @Override
    public PrescriptionResponseDto getPrescriptionById(Long prescriptionId) {

        Prescription prescription = prescriptionRepository.findById(prescriptionId)
                .orElseThrow(() ->
                        new RuntimeException("Prescription not found"));

        return mapToResponse(prescription);
    }

    @Override
    public PrescriptionResponseDto getPrescriptionByAppointment(Long appointmentId) {

        Appointment appointment = appointmentRepository.findById(appointmentId)
                .orElseThrow(() ->
                        new RuntimeException("Appointment not found"));

        Prescription prescription = prescriptionRepository.findByAppointment(appointment)
                .orElseThrow(() ->
                        new RuntimeException("Prescription not found"));

        return mapToResponse(prescription);
    }

    @Override
    public List<PrescriptionResponseDto> getPrescriptionsByPatient(Long patientId) {

        List<Prescription> prescriptions =
                prescriptionRepository.findByPatientPatientId(patientId);

        return prescriptions.stream()
                .map(this::mapToResponse)
                .toList();
    }

    @Override
    public List<PrescriptionResponseDto> getPrescriptionsByDoctor(Long doctorId) {

        List<Prescription> prescriptions =
                prescriptionRepository.findByDoctorId(doctorId);

        return prescriptions.stream()
                .map(this::mapToResponse)
                .toList();
    }

    @Override
    public List<PrescriptionResponseDto> getAllPrescriptions() {

        return prescriptionRepository.findAll()
                .stream()
                .map(this::mapToResponse)
                .toList();
    }

    private PrescriptionResponseDto mapToResponse(Prescription prescription) {

        return PrescriptionResponseDto.builder()
                .prescriptionId(prescription.getPrescriptionId())

                .appointmentId(
                        prescription.getAppointment().getAppointmentId())

                .doctorId(
                        prescription.getDoctor().getId())

                .doctorName(
                        prescription.getDoctor().getFirstName()
                                + " "
                                + prescription.getDoctor().getLastName())

                .patientId(
                        prescription.getPatient().getPatientId())

                .patientName(
                        prescription.getPatient().getFirstName()
                                + " "
                                + prescription.getPatient().getLastName())

                .diagnosis(prescription.getDiagnosis())
                .medicines(prescription.getMedicines())
                .advice(prescription.getAdvice())
                .prescriptionHtml(prescription.getPrescriptionHtml())
                .createdAt(prescription.getCreatedAt())

                .build();
    }
}