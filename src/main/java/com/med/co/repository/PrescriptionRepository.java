package com.med.co.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.med.co.entity.Appointment;
import com.med.co.entity.Prescription;

@Repository
public interface PrescriptionRepository extends JpaRepository<Prescription, Long> {

    // Get prescription by appointment
    Optional<Prescription> findByAppointment(Appointment appointment);

    // Check if a prescription already exists for an appointment
    boolean existsByAppointment(Appointment appointment);

    // Get all prescriptions of a patient
    List<Prescription> findByPatientPatientId(Long patientId);

    // Get all prescriptions written by a doctor
    List<Prescription> findByDoctorId(Long doctorId);

}