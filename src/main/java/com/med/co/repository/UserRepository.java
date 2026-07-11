package com.med.co.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.med.co.entity.UserRole;

@Repository
public interface UserRepository extends JpaRepository<UserRole, Long> {

    Optional<UserRole> findByEmail(String email);

    boolean existsByEmail(String email);

}