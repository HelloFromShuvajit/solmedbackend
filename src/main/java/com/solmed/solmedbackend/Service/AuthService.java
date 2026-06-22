package com.solmed.solmedbackend.Service;

import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import com.solmed.solmedbackend.caretaker.CaretakerService;
import com.solmed.solmedbackend.user.User;
import com.solmed.solmedbackend.user.UserRepository;
import com.solmed.solmedbackend.user.UserRole;
import com.solmed.solmedbackend.jwt.*;
import com.solmed.solmedbackend.DTOs.*;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;
    private final AuthenticationManager authenticationManager;
    private final CaretakerService caretakerService;

    public AuthResponse register(RegisterRequest request) {
        UserRole role = request.getRole() != null ? request.getRole() : UserRole.OWNER;
        if (role == UserRole.CARETAKER) {
            if (request.getPatientEmail() == null || request.getPatientEmail().isBlank()) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                        "Patient email is required when registering as a caretaker.");
            }
        }

        User user = new User();
        user.setEmail(request.getEmail());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setAge(request.getAge());
        user.setGender(request.getGender());
        user.setName(request.getName());
        user.setPhone(request.getPhone());
        user.setPosition(request.getPosition() != null ? request.getPosition() : "User");
        user.setRole(role == UserRole.CARETAKER ? UserRole.CARETAKER : UserRole.OWNER);

        userRepository.save(user);

        if (role == UserRole.CARETAKER) {
            caretakerService.createPendingLinkRequest(user, request.getPatientEmail());
        }

        String token = jwtUtil.generateToken(user);
        return new AuthResponse(token);
    }

    public AuthResponse login(LoginRequest request) {
        authenticationManager.authenticate(
            new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword())
        );
        User user = userRepository.findByEmail(request.getEmail()).orElseThrow();
        String token = jwtUtil.generateToken(user);
        return new AuthResponse(token);
    }
}
