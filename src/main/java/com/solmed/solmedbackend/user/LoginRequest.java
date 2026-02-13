package com.solmed.solmedbackend.user;
import lombok.Data;

@Data
public class LoginRequest {
    private String email;
    private String password;
}
