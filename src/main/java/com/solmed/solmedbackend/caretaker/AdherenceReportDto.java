package com.solmed.solmedbackend.caretaker;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AdherenceReportDto {
    private List<AdherenceDayDto> days;
}
