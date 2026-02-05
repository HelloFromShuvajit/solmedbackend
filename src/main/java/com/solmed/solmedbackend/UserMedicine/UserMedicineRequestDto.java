package com.solmed.solmedbackend.UserMedicine;

import java.time.LocalTime;

import lombok.Data;

@Data
public class UserMedicineRequestDto {

    private Long userId;
    private Long medicineId;
    private LocalTime inputTime;


}