package com.med.co.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.med.co.entity.Department;

public interface DepartmentRepository extends JpaRepository<Department, Long> {

    boolean existsByDepartmentName(String departmentName);

}