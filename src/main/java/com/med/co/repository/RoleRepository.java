package com.med.co.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.med.co.entity.Role;
import com.med.co.enums.Enums.RoleType;

@Repository
public interface RoleRepository extends JpaRepository<Role, Long> {

    Optional<Role> findByRoleName(RoleType roleName);

}