package com.example.taskmanagement.auth.service;

import com.example.taskmanagement.auth.dto.AuthResponse;
import com.example.taskmanagement.auth.dto.LoginRequest;
import com.example.taskmanagement.auth.dto.RegisterRequest;

public interface AuthService {

    AuthResponse register(RegisterRequest request);

    AuthResponse login(LoginRequest request);
}
