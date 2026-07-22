package com.med.co.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import com.med.co.entity.DoctorLeave;

public interface DoctorLeaveRepository extends JpaRepository<DoctorLeave, Long> {

    List<DoctorLeave> findByDoctorId(Long doctorId);

}