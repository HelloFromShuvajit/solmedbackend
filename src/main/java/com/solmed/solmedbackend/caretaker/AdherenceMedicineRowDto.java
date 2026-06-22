package com.solmed.solmedbackend.caretaker;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AdherenceMedicineRowDto {
    private String name;
    private String status;
    private String scheduledTime;
    private String takenAt;
}
