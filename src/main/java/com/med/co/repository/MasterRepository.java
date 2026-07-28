package com.med.co.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.med.co.entity.Master;

public interface MasterRepository extends JpaRepository<Master, Long> {

    List<Master> findByMrnNo(String mrnNo);

}