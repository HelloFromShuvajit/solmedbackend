package com.solmed.solmedbackend.caretaker;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CaretakerSummaryDto {
    private Long caretakerUserId;
    private String email;
    private String name;
}
