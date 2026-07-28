package com.med.co.repository;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.med.co.entity.DoctorLeave;
import com.med.co.enums.LeaveStatus;

public interface DoctorLeaveRepository extends JpaRepository<DoctorLeave, Long> {

    List<DoctorLeave> findByDoctorId(Long doctorId);

    // Count distinct doctors who have an approved leave that covers the given date
    @Query("SELECT COUNT(DISTINCT dl.doctor.id) FROM DoctorLeave dl "
            + "WHERE dl.status = :status "
            + "AND :date BETWEEN dl.fromDate AND dl.toDate")
    long countDistinctDoctorsOnLeaveByStatusAndDate(@Param("status") LeaveStatus status,
                                                    @Param("date") LocalDate date);

    // Find all approved leaves that have ended (toDate is in the past)
    @Query("SELECT dl FROM DoctorLeave dl WHERE dl.status = :status AND dl.toDate < CURRENT_DATE")
    List<DoctorLeave> findExpiredLeaves(@Param("status") LeaveStatus status);

    // Count total number of leaves
    @Query("SELECT COUNT(dl) FROM DoctorLeave dl")
    long countTotalLeaves();

    // Count leaves by status
    @Query("SELECT COUNT(dl) FROM DoctorLeave dl WHERE dl.status = :status")
    long countByStatus(@Param("status") LeaveStatus status);

    // Count currently active leaves (approved leaves that are ongoing as of today)
    @Query("SELECT COUNT(dl) FROM DoctorLeave dl "
            + "WHERE dl.status = :status "
            + "AND CURRENT_DATE BETWEEN dl.fromDate AND dl.toDate")
    long countActiveLeaves(@Param("status") LeaveStatus status);

    // Get distinct doctor IDs on approved leave for a specific date
    @Query("SELECT DISTINCT dl.doctor.id FROM DoctorLeave dl "
            + "WHERE dl.status = 'APPROVED' "
            + "AND :date BETWEEN dl.fromDate AND dl.toDate")
    List<Long> findDoctorIdsOnLeave(@Param("date") LocalDate date);

}