package com.solmed.solmedbackend.UserMedicine;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;


public interface UserMedicineRepository extends JpaRepository<UserMedicine, Long>{
    List<UserMedicine> findByUser_Id(Long userId);
}