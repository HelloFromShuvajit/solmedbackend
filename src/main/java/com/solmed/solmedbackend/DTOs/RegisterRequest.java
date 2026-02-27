package com.solmed.solmedbackend.DTOs;

import lombok.Data;

@Data
public class RegisterRequest {
    private String email;
    private String password;
    private String name;
    private String position;
    private String phone;
    private Integer age;
}