package com.med.co.repository;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.med.co.entity.Appointment;
import com.med.co.entity.Doctor;
import com.med.co.entity.Patient;
import com.med.co.enums.AppointmentStatus;

@Repository
public interface AppointmentRepository extends JpaRepository<Appointment, Long> {

    // Get all appointments of a doctor
    List<Appointment> findByDoctor(Doctor doctor);

    // Get all appointments of a patient
    List<Appointment> findByPatient(Patient patient);

    // Get appointments of a doctor on a particular date
    List<Appointment> findByDoctorAndAppointmentDate(
            Doctor doctor,
            LocalDate appointmentDate);

    // Get appointments of a patient on a particular date
    List<Appointment> findByPatientAndAppointmentDate(
            Patient patient,
            LocalDate appointmentDate);

    // Check whether a slot is already booked
    Optional<Appointment> findByDoctorAndAppointmentDateAndAppointmentTime(
            Doctor doctor,
            LocalDate appointmentDate,
            LocalTime appointmentTime);

    // Check whether a slot is already booked with a particular status
    boolean existsByDoctorAndAppointmentDateAndAppointmentTimeAndStatus(
            Doctor doctor,
            LocalDate appointmentDate,
            LocalTime appointmentTime,
            AppointmentStatus status);

    // Get all appointments by status
    List<Appointment> findByStatus(AppointmentStatus status);

    // Doctor appointments by status
    List<Appointment> findByDoctorAndStatus(
            Doctor doctor,
            AppointmentStatus status);

    // Patient appointments by status
    List<Appointment> findByPatientAndStatus(
            Patient patient,
            AppointmentStatus status);

}