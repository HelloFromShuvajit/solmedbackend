package com.solmed.solmedbackend.caretaker;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PatientSummaryDto {
    private Long ownerId;
    private String name;
    private String email;
    private int todayTaken;
    private int todayMissed;
    private int todayPending;
}
