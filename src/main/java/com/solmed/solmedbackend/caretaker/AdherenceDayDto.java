package com.solmed.solmedbackend.caretaker;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AdherenceDayDto {
    private String date;
    private int totalDoses;
    private int takenDoses;
    private int missedDoses;
    private List<AdherenceMedicineRowDto> medicines;
}
