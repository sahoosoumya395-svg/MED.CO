package com.med.co.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.med.co.entity.DoctorAvailability;

import java.time.LocalDate;
import java.util.Optional;



@Repository
public interface DoctorAvailabilityRepository extends JpaRepository<DoctorAvailability, Long> {

    // Get all availability slots of a doctor
    List<DoctorAvailability> findByDoctorId(Long doctorId);
    
    Optional<DoctorAvailability> findByDoctorIdAndAvailableDateAndAvailableTrue(
            Long doctorId,
            LocalDate availableDate);

    // Get all doctors available on a specific date
    List<DoctorAvailability> findByAvailableDateAndAvailableTrue(LocalDate availableDate);

    // Get distinct doctor IDs available on a specific date
    @org.springframework.data.jpa.repository.Query("SELECT DISTINCT da.doctor.id FROM DoctorAvailability da " +
           "WHERE da.availableDate = :date AND da.available = true")
    List<Long> findDistinctDoctorIdsAvailableOn(@org.springframework.data.repository.query.Param("date") LocalDate date);

}