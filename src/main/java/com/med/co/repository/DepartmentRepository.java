package com.med.co.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.med.co.dto.response.DepartmentAvailabilityResponseDto;
import com.med.co.entity.Department;

public interface DepartmentRepository extends JpaRepository<Department, Long> {

    boolean existsByDepartmentName(String departmentName);

    @Query("""
        SELECT new com.med.co.dto.response.DepartmentAvailabilityResponseDto(
            d.departmentId,
            d.departmentName,
            COUNT(doc.id),
            SUM(
                CASE
                    WHEN doc.status = com.med.co.enums.Enums.DoctorStatus.AVAILABLE
                    THEN 1L
                    ELSE 0L
                END
            )
        )
        FROM Department d
        LEFT JOIN d.doctors doc
        GROUP BY d.departmentId, d.departmentName
    """)
    List<DepartmentAvailabilityResponseDto> getDepartmentAvailability();

}