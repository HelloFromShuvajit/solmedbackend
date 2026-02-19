package com.solmed.solmedbackend.UserMedicine;

import java.time.LocalTime;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

@Data
public class UserMedicineRequestDto {

    private Long userId;
    private Long medicineId;
    @JsonFormat(pattern = "HH:mm")
    private LocalTime inputTime;

}