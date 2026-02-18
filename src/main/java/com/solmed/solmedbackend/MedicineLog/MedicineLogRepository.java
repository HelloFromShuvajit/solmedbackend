package com.solmed.solmedbackend.MedicineLog;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;


public interface MedicineLogRepository extends JpaRepository<MedicineLog, Long> {

    List<MedicineLog> findByUserMedId(Long userMedId);
    
}
