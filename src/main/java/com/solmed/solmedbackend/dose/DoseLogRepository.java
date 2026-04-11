package com.solmed.solmedbackend.dose;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

public interface DoseLogRepository extends JpaRepository<DoseLog, Long> {

    List<DoseLog> findByUser_IdAndDateBetween(Long userId, LocalDate start, LocalDate end);

    List<DoseLog> findByUser_IdAndDate(Long userId, LocalDate date);

    List<DoseLog> findByUserMedicineIdAndDate(Long userMedicineId, LocalDate date);

    List<DoseLog> findByStatus(DoseStatus status);
}
