package com.solmed.solmedbackend.medicine;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

public interface MedicineRepository extends JpaRepository<Medicine, Long> {

    Optional<Medicine> findBymedName(String medName);

}
