package com.med.co.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.med.co.entity.Doctor;

@Repository
public interface DoctorRepository extends JpaRepository<Doctor, Long> {

    java.util.Optional<Doctor> findByUserrole(com.med.co.entity.UserRole userrole);

    boolean existsByEmail(String email);


    boolean existsByEmailAndIdNot(
            String email,
            Long id
    );


    boolean existsByMobileNumber(String mobileNumber);


    boolean existsByMobileNumberAndIdNot(
            String mobileNumber,
            Long id
    );


    boolean existsByMedicalRegistrationNumber(
            String medicalRegistrationNumber
    );


    boolean existsByMedicalRegistrationNumberAndIdNot(
            String medicalRegistrationNumber,
            Long id
    );

}