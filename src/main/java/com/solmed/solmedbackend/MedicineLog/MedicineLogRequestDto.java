package com.solmed.solmedbackend.MedicineLog;

import lombok.Data;

@Data
public class MedicineLogRequestDto {
    private Long medLogId;
    private String message;

}
