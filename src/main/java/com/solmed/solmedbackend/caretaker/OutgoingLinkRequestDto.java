package com.solmed.solmedbackend.caretaker;

import java.time.Instant;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class OutgoingLinkRequestDto {
    private Long requestId;
    private String patientEmail;
    private String patientName;
    private InviteStatus status;
    private Instant createdAt;
}
