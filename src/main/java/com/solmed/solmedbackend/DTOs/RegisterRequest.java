package com.solmed.solmedbackend.DTOs;

import com.solmed.solmedbackend.user.UserRole;

import lombok.Data;

@Data
public class RegisterRequest {
    private String email;
    private String password;
    private String name;
    private String position;
    private String phone;
    private Integer age;
    private String gender;
    /** Defaults to OWNER when omitted. */
    private UserRole role;
    /** Required when role is CARETAKER: patient (owner) email to request linking. */
    private String patientEmail;
}
