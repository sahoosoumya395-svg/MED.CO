package com.med.co.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.med.co.entity.Doctor;

@Repository
public interface DoctorRepository extends JpaRepository<Doctor, Long> {

    Optional<Doctor> findByEmail(String email);

    Optional<Doctor> findByMobileNumber(String mobileNumber);

    Optional<Doctor> findByMedicalRegistrationNumber(String medicalRegistrationNumber);

    boolean existsByEmail(String email);

    boolean existsByMobileNumber(String mobileNumber);

    boolean existsByMedicalRegistrationNumber(String medicalRegistrationNumber);
}