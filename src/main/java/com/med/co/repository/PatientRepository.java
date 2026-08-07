package com.med.co.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.med.co.entity.Patient;

@Repository
public interface PatientRepository extends JpaRepository<Patient, Long> {


   

    Optional<Patient> findByMrnNo(String mrnNo);

	boolean existsByMrnNo(String mrnNo);
	
	java.util.Optional<Patient> findByUserrole(com.med.co.entity.UserRole userrole);

	@Query("SELECT COUNT(DISTINCT a.patient.patientId) FROM Appointment a WHERE LOWER(a.doctor.department.departmentName) = LOWER(:departmentName)")
	long countPatientsByDepartmentName(@Param("departmentName") String departmentName);


}