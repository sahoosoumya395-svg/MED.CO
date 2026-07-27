package com.med.co.serviceimpl;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.med.co.dto.request.DoctorAvailabilityRequest;
import com.med.co.dto.response.ApiResponse;
import com.med.co.dto.response.AvailableDoctorsCountResponse;
import com.med.co.entity.Doctor;
import com.med.co.entity.DoctorAvailability;
import com.med.co.entity.DoctorRecurringAvailability;
import com.med.co.enums.LeaveStatus;
import com.med.co.repository.DoctorAvailabilityRepository;
import com.med.co.repository.DoctorRecurringAvailabilityRepository;
import com.med.co.repository.DoctorLeaveRepository;
import com.med.co.repository.DoctorRepository;
import com.med.co.service.DoctorAvailabilityService;

@Service
public class DoctorAvailabilityServiceImpl implements DoctorAvailabilityService {

    @Autowired
    private DoctorAvailabilityRepository availabilityRepository;

    @Autowired
    private DoctorRecurringAvailabilityRepository recurringAvailabilityRepository;

    @Autowired
    private DoctorLeaveRepository leaveRepository;

    @Autowired
    private DoctorRepository doctorRepository;

    @Autowired
    private ModelMapper modelMapper;

    @Override
    public ApiResponse<?> addAvailability(DoctorAvailabilityRequest request) {

        Doctor doctor = doctorRepository.findById(request.getDoctorId())
                .orElseThrow(() -> new RuntimeException("Doctor not found"));

        DoctorAvailability availability = new DoctorAvailability();

        availability.setDoctor(doctor);
        availability.setAvailableDate(request.getAvailableDate());
        availability.setStartTime(request.getStartTime());
        availability.setEndTime(request.getEndTime());
        availability.setAvailable(request.getAvailable());

        availabilityRepository.save(availability);

        return new ApiResponse<>(201, "Doctor availability added successfully", availability);
    }

    @Override
    public ApiResponse<?> getAvailabilityByDoctor(Long doctorId) {

        List<DoctorAvailability> list = availabilityRepository.findByDoctorId(doctorId);

        return new ApiResponse<>(200, "Doctor availability fetched successfully", list);
    }

    @Override
    public ApiResponse<?> updateAvailability(Long availabilityId,
            DoctorAvailabilityRequest request) {

        DoctorAvailability availability = availabilityRepository.findById(availabilityId)
                .orElseThrow(() -> new RuntimeException("Availability not found"));

        availability.setAvailableDate(request.getAvailableDate());
        availability.setStartTime(request.getStartTime());
        availability.setEndTime(request.getEndTime());
        availability.setAvailable(request.getAvailable());

        availabilityRepository.save(availability);

        return new ApiResponse<>(200, "Doctor availability updated successfully", availability);
    }

    @Override
    public ApiResponse<?> deleteAvailability(Long availabilityId) {

        DoctorAvailability availability = availabilityRepository.findById(availabilityId)
                .orElseThrow(() -> new RuntimeException("Availability not found"));

        availabilityRepository.delete(availability);

        return new ApiResponse<>(200, "Doctor availability deleted successfully", null);
    }

    @Override
    public AvailableDoctorsCountResponse countAvailableDoctorsOn(LocalDate date) {
        // Step 1: Get doctors with specific date availability
        List<Long> specificDoctorIds = availabilityRepository.findDistinctDoctorIdsAvailableOn(date);
        Set<Long> allScheduledDoctorIds = new HashSet<>(specificDoctorIds);

        // Step 2: Get doctors with recurring availability matching this date
        // MONTHLY_BY_DAY
        int dayOfMonth = date.getDayOfMonth();
        List<Long> monthlyDoctorIds = recurringAvailabilityRepository.findDoctorIdsMonthlyByDay(dayOfMonth, date);
        allScheduledDoctorIds.addAll(monthlyDoctorIds);

        // WEEKLY_BY_DAY
        int dayOfWeek = date.getDayOfWeek().getValue(); // 1=MON, 7=SUN
        List<Long> weeklyDoctorIds = recurringAvailabilityRepository.findDoctorIdsWeeklyByDay(dayOfWeek, date);
        allScheduledDoctorIds.addAll(weeklyDoctorIds);

        // DAILY
        List<Long> dailyDoctorIds = recurringAvailabilityRepository.findDoctorIdsDailyRecurrences(date);
        allScheduledDoctorIds.addAll(dailyDoctorIds);

        // Step 3: Get doctors on approved leave
        long doctorsOnLeaveCount = leaveRepository.countDistinctDoctorsOnLeaveByStatusAndDate(LeaveStatus.APPROVED, date);

        // Step 4: Get the list of doctors on leave (to exclude them)
        List<Long> leaveDoctorIds = leaveRepository.findDoctorIdsOnLeave(date);
        Set<Long> leaveDoctorIdSet = new HashSet<>(leaveDoctorIds);

        // Step 5: Calculate available doctors (scheduled - on leave)
        Set<Long> availableDoctorIds = new HashSet<>(allScheduledDoctorIds);
        availableDoctorIds.removeAll(leaveDoctorIdSet);

        // Build response
        return AvailableDoctorsCountResponse.builder()
                .date(date)
                .availableCount(availableDoctorIds.size())
                .totalScheduledRecurring(monthlyDoctorIds.size() + weeklyDoctorIds.size() + dailyDoctorIds.size())
                .totalScheduledSpecific(specificDoctorIds.size())
                .totalOnLeaveApproved((int) doctorsOnLeaveCount)
                .message("Available doctors count calculated successfully")
                .build();
    }
}