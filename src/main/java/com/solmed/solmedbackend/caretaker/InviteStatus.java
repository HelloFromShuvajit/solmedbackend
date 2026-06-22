package com.solmed.solmedbackend.caretaker;

public enum InviteStatus {
    PENDING,
    ACCEPTED,
    REJECTED,
    /** Legacy / unused for link requests */
    REVOKED
}
