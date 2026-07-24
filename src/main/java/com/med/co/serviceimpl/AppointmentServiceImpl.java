package com.med.co.serviceimpl;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.med.co.dto.request.AppointmentRequestDto;
import com.med.co.dto.response.AppointmentResponseDto;
import com.med.co.entity.Appointment;
import com.med.co.entity.Doctor;
import com.med.co.entity.DoctorAvailability;
import com.med.co.entity.DoctorLeave;
import com.med.co.entity.Patient;
import com.med.co.enums.AppointmentStatus;
import com.med.co.enums.LeaveStatus;
import com.med.co.repository.AppointmentRepository;
import com.med.co.repository.DoctorAvailabilityRepository;
import com.med.co.repository.DoctorLeaveRepository;
import com.med.co.repository.DoctorRepository;
import com.med.co.repository.PatientRepository;
import com.med.co.service.AppointmentService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AppointmentServiceImpl implements AppointmentService {

	private final AppointmentRepository appointmentRepository;
	private final DoctorRepository doctorRepository;
	private final PatientRepository patientRepository;
	private final DoctorAvailabilityRepository availabilityRepository;
	private final DoctorLeaveRepository leaveRepository;

	@Override
    public AppointmentResponseDto bookAppointment(AppointmentRequestDto requestDto) {

        // Check Doctor
        Doctor doctor = doctorRepository.findById(requestDto.getDoctorId())
                .orElseThrow(() ->
                        new RuntimeException("Doctor not found"));

        // Check Patient
        Patient patient = patientRepository.findById(requestDto.getPatientId())
                .orElseThrow(() ->
                        new RuntimeException("Patient not found"));

        // Check Doctor Availability Day
        DayOfWeek day = requestDto.getAppointmentDate().getDayOfWeek();

        List<DoctorAvailability> availabilityList =
                availabilityRepository.findByDoctorId(doctor.getId());

        boolean available = availabilityList.stream()
                .anyMatch(a ->
                        a.getAvailable()
                        && a.getDayOfWeek().equals(day)
                        && !requestDto.getAppointmentTime().isBefore(a.getStartTime())
                        && !requestDto.getAppointmentTime().isAfter(a.getEndTime()));

        if (!available) {
            throw new RuntimeException(
                    "Doctor is not available on selected day/time");
        }

        // Check Doctor Leave
        List<DoctorLeave> leaves =
                leaveRepository.findByDoctorId(doctor.getId());

        boolean onLeave = leaves.stream()
                .anyMatch(l ->
                        l.getStatus() == LeaveStatus.APPROVED
                                && !requestDto.getAppointmentDate()
                                        .isBefore(l.getFromDate())
                                && !requestDto.getAppointmentDate()
                                        .isAfter(l.getToDate()));

        if (onLeave) {
            throw new RuntimeException(
                    "Doctor is on leave on selected date");
        }

        // Check Existing Appointment
        boolean slotBooked =
                appointmentRepository
                        .existsByDoctorAndAppointmentDateAndAppointmentTimeAndStatus(
                                doctor,
                                requestDto.getAppointmentDate(),
                                requestDto.getAppointmentTime(),
                                AppointmentStatus.BOOKED);

        if (slotBooked) {
            throw new RuntimeException(
                    "Selected slot already booked");
        }

        // Save Appointment
        Appointment appointment = Appointment.builder()
                .doctor(doctor)
                .patient(patient)
                .appointmentDate(requestDto.getAppointmentDate())
                .appointmentTime(requestDto.getAppointmentTime())
                .reason(requestDto.getReason())
                .status(AppointmentStatus.BOOKED)
                .build();

        Appointment saved =
                appointmentRepository.save(appointment);

        return mapToResponse1(saved);
    }
	
    private AppointmentResponseDto mapToResponse1(Appointment appointment) {

        return AppointmentResponseDto.builder()
                .appointmentId(appointment.getAppointmentId())

                .doctorId(appointment.getDoctor().getId())
                .doctorName(
                        appointment.getDoctor().getFirstName()
                                + " "
                                + appointment.getDoctor().getLastName())

                .patientId(appointment.getPatient().getPatientId())
                .patientName(
                        appointment.getPatient().getFirstName()
                                + " "
                                + appointment.getPatient().getLastName())

                .appointmentDate(appointment.getAppointmentDate())
                .appointmentTime(appointment.getAppointmentTime())
                .reason(appointment.getReason())
                .status(appointment.getStatus())
                .createdAt(appointment.getCreatedAt())
                .updatedAt(appointment.getUpdatedAt())
                .build();
    }
    
    @Override
    public AppointmentResponseDto cancelAppointment(Long appointmentId) {

        Appointment appointment = appointmentRepository.findById(appointmentId)
                .orElseThrow(() ->
                        new RuntimeException("Appointment not found"));

        if (appointment.getStatus() == AppointmentStatus.CANCELLED) {
            throw new RuntimeException("Appointment is already cancelled.");
        }

        appointment.setStatus(AppointmentStatus.CANCELLED);

        Appointment updatedAppointment = appointmentRepository.save(appointment);

        return mapToResponse1(updatedAppointment);
    }

    @Override
    public AppointmentResponseDto getAppointmentById(Long appointmentId) {

        Appointment appointment = appointmentRepository.findById(appointmentId)
                .orElseThrow(() ->
                        new RuntimeException("Appointment not found"));

        return mapToResponse1(appointment);
    }

    @Override
    public List<AppointmentResponseDto> getAppointmentsByDoctor(Long doctorId) {

        Doctor doctor = doctorRepository.findById(doctorId)
                .orElseThrow(() ->
                        new RuntimeException("Doctor not found"));

        List<Appointment> appointments =
                appointmentRepository.findByDoctor(doctor);

        return appointments.stream()
                .map(this::mapToResponse1)
                .toList();
    }

    @Override
    public List<AppointmentResponseDto> getAppointmentsByPatient(Long patientId) {

        Patient patient = patientRepository.findById(patientId)
                .orElseThrow(() ->
                        new RuntimeException("Patient not found"));

        List<Appointment> appointments =
                appointmentRepository.findByPatient(patient);

        return appointments.stream()
                .map(this::mapToResponse1)
                .toList();
    }
    
    @Override
    public List<LocalDate> getAvailableDates(Long doctorId) {

        Doctor doctor = doctorRepository.findById(doctorId)
                .orElseThrow(() ->
                        new RuntimeException("Doctor not found"));

        List<DoctorAvailability> availabilityList =
                availabilityRepository.findByDoctorId(doctorId);

        List<DoctorLeave> leaveList =
                leaveRepository.findByDoctorId(doctorId);

        LocalDate today = LocalDate.now();
        LocalDate endDate = today.plusDays(30);

        List<LocalDate> availableDates = new ArrayList<>();

        while (!today.isAfter(endDate)) {

            LocalDate currentDate = today;

            DayOfWeek day = currentDate.getDayOfWeek();

            boolean available = availabilityList.stream()
                    .anyMatch(a ->
                            a.getAvailable()
                            && a.getDayOfWeek().equals(day));

            boolean onLeave = leaveList.stream()
                    .anyMatch(l ->
                            l.getStatus() == LeaveStatus.APPROVED
                            && !currentDate.isBefore(l.getFromDate())
                            && !currentDate.isAfter(l.getToDate()));

            if (available && !onLeave) {
                availableDates.add(currentDate);
            }

            today = today.plusDays(1);
        }

        return availableDates;
    }

    @Override
    public List<LocalTime> getAvailableTimeSlots(Long doctorId,
                                                 LocalDate appointmentDate) {

        Doctor doctor = doctorRepository.findById(doctorId)
                .orElseThrow(() ->
                        new RuntimeException("Doctor not found"));

        DayOfWeek day = appointmentDate.getDayOfWeek();

        List<DoctorAvailability> availabilityList =
                availabilityRepository.findByDoctorId(doctorId);

        Optional<DoctorAvailability> availability =
                availabilityList.stream()
                        .filter(a ->
                                a.getAvailable()
                                        && a.getDayOfWeek().equals(day))
                        .findFirst();

        if (availability.isEmpty()) {
            throw new RuntimeException(
                    "Doctor is not available on selected day.");
        }

        DoctorAvailability slot = availability.get();

        List<Appointment> bookedAppointments =
                appointmentRepository.findByDoctorAndAppointmentDate(
                        doctor,
                        appointmentDate);

        List<LocalTime> bookedSlots = bookedAppointments.stream()
                .filter(a ->
                        a.getStatus() == AppointmentStatus.BOOKED)
                .map(Appointment::getAppointmentTime)
                .toList();

        List<LocalTime> availableSlots = new ArrayList<>();

        LocalTime current = slot.getStartTime();

        while (current.isBefore(slot.getEndTime())) {

            if (!bookedSlots.contains(current)) {
                availableSlots.add(current);
            }

            current = current.plusMinutes(30);
        }

        return availableSlots;
    }

    private AppointmentResponseDto mapToResponse(Appointment appointment) {

        return AppointmentResponseDto.builder()

                .appointmentId(appointment.getAppointmentId())

                .doctorId(appointment.getDoctor().getId())
                .doctorName(
                        appointment.getDoctor().getFirstName()
                                + " "
                                + appointment.getDoctor().getLastName())

                .patientId(appointment.getPatient().getPatientId())
                .patientName(
                        appointment.getPatient().getFirstName()
                                + " "
                                + appointment.getPatient().getLastName())

                .appointmentDate(appointment.getAppointmentDate())
                .appointmentTime(appointment.getAppointmentTime())
                .reason(appointment.getReason())
                .status(appointment.getStatus())
                .createdAt(appointment.getCreatedAt())
                .updatedAt(appointment.getUpdatedAt())

                .build();
    }

}
	