package com.yosiefgobeze.videorentalsystem.controller;


import com.yosiefgobeze.videorentalsystem.dto.RegisterRequest;
import com.yosiefgobeze.videorentalsystem.model.Role;
import com.yosiefgobeze.videorentalsystem.model.User;
import com.yosiefgobeze.videorentalsystem.repository.UserRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public AuthController(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @PostMapping("/register")
    public ResponseEntity<?> registerUser(@RequestBody RegisterRequest registration) {
        if (userRepository.findByEmail(registration.getEmail()).isPresent()) {
            return ResponseEntity.badRequest().body(Map.of("error", "Email is already taken!"));
        }

        Role assignedRole = (registration.getRole() == null) ? Role.CUSTOMER : registration.getRole();

        User user = User.builder()
                .email(registration.getEmail())
                .password(passwordEncoder.encode(registration.getPassword())) // BCrypt hashing
                .firstName(registration.getFirstName())
                .lastName(registration.getLastName())
                .role(assignedRole)
                .build();

        userRepository.save(user);
        return new ResponseEntity<>(Map.of("message", "User registered successfully"), HttpStatus.CREATED);
    }

    @GetMapping("/login")
    public ResponseEntity<?> login() {
        // Handled automatically via Spring Security Basic Auth filter chain.
        // If authentication passes, this endpoint returns a 200 OK.
        return ResponseEntity.ok(Map.of("message", "Logged in successfully"));
    }
}
