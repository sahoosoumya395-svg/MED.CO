package com.med.co.repository;

import java.util.List;
import java.time.LocalDate;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.med.co.entity.DoctorRecurringAvailability;
import com.med.co.entity.DoctorRecurringAvailability.RecurrenceType;

@Repository
public interface DoctorRecurringAvailabilityRepository extends JpaRepository<DoctorRecurringAvailability, Long> {

    // Find monthly recurring availabilities for a specific day of month
    @Query("SELECT r FROM DoctorRecurringAvailability r " +
           "WHERE r.recurrenceType = 'MONTHLY_BY_DAY' " +
           "AND r.dayOfMonth = :dayOfMonth " +
           "AND (r.startDate IS NULL OR r.startDate <= :date) " +
           "AND (r.endDate IS NULL OR r.endDate >= :date) " +
           "AND r.active = true")
    List<DoctorRecurringAvailability> findMonthlyByDay(@Param("dayOfMonth") int dayOfMonth,
                                                       @Param("date") LocalDate date);

    // Find weekly recurring availabilities for a specific day of week
    @Query("SELECT r FROM DoctorRecurringAvailability r " +
           "WHERE r.recurrenceType = 'WEEKLY_BY_DAY' " +
           "AND r.dayOfWeek = :dayOfWeek " +
           "AND (r.startDate IS NULL OR r.startDate <= :date) " +
           "AND (r.endDate IS NULL OR r.endDate >= :date) " +
           "AND r.active = true")
    List<DoctorRecurringAvailability> findWeeklyByDay(@Param("dayOfWeek") int dayOfWeek,
                                                      @Param("date") LocalDate date);

    // Find daily recurring availabilities
    @Query("SELECT r FROM DoctorRecurringAvailability r " +
           "WHERE r.recurrenceType = 'DAILY' " +
           "AND (r.startDate IS NULL OR r.startDate <= :date) " +
           "AND (r.endDate IS NULL OR r.endDate >= :date) " +
           "AND r.active = true")
    List<DoctorRecurringAvailability> findDailyRecurrences(@Param("date") LocalDate date);

    // Find all by doctor id
    List<DoctorRecurringAvailability> findByDoctorId(Long doctorId);

    // Get distinct doctor IDs that have monthly recurring availability on a specific day
    @Query("SELECT DISTINCT r.doctor.id FROM DoctorRecurringAvailability r " +
           "WHERE r.recurrenceType = 'MONTHLY_BY_DAY' " +
           "AND r.dayOfMonth = :dayOfMonth " +
           "AND (r.startDate IS NULL OR r.startDate <= :date) " +
           "AND (r.endDate IS NULL OR r.endDate >= :date) " +
           "AND r.active = true")
    List<Long> findDoctorIdsMonthlyByDay(@Param("dayOfMonth") int dayOfMonth,
                                         @Param("date") LocalDate date);

    // Get distinct doctor IDs that have weekly recurring availability on a specific day of week
    @Query("SELECT DISTINCT r.doctor.id FROM DoctorRecurringAvailability r " +
           "WHERE r.recurrenceType = 'WEEKLY_BY_DAY' " +
           "AND r.dayOfWeek = :dayOfWeek " +
           "AND (r.startDate IS NULL OR r.startDate <= :date) " +
           "AND (r.endDate IS NULL OR r.endDate >= :date) " +
           "AND r.active = true")
    List<Long> findDoctorIdsWeeklyByDay(@Param("dayOfWeek") int dayOfWeek,
                                        @Param("date") LocalDate date);

    // Get distinct doctor IDs that have daily recurring availability
    @Query("SELECT DISTINCT r.doctor.id FROM DoctorRecurringAvailability r " +
           "WHERE r.recurrenceType = 'DAILY' " +
           "AND (r.startDate IS NULL OR r.startDate <= :date) " +
           "AND (r.endDate IS NULL OR r.endDate >= :date) " +
           "AND r.active = true")
    List<Long> findDoctorIdsDailyRecurrences(@Param("date") LocalDate date);
}
