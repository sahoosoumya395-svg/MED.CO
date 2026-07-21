package com.med.co.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.med.co.entity.RevokedToken;

@Repository
public interface RevokedTokenRepository extends JpaRepository<RevokedToken, Long> {

    Optional<RevokedToken> findByToken(String token);

    boolean existsByToken(String token);

}