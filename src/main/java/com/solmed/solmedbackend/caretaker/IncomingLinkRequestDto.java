package com.solmed.solmedbackend.caretaker;

import java.time.Instant;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class IncomingLinkRequestDto {
    private Long requestId;
    private Long caretakerUserId;
    private String caretakerEmail;
    private String caretakerName;
    private Instant createdAt;
}
