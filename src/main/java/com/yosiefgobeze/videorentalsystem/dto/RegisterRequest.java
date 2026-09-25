package com.yosiefgobeze.videorentalsystem.dto;

import com.yosiefgobeze.videorentalsystem.model.Role;

import lombok.Data;

@Data
public class RegisterRequest {
    private String email;
    private String password;
    private String firstName;
    private String lastName;
    private Role role; // Optional, will default to CUSTOMER if null
}
