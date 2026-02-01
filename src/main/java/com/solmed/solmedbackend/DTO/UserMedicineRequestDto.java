package com.solmed.solmedbackend.DTO;

import java.time.LocalTime;

public class UserMedicineRequestDto {

    private Long userId;
    private Long medicineId;
    private LocalTime inputTime;

    
    public Long getUserId() {
        return userId;
    }
    public Long getMedicineId() {
        return medicineId;
    }
    public LocalTime getInputTime() {
        return inputTime;
    }


}