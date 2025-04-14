package com.example.ambutracker.service;

import com.example.ambutracker.dto.AuthenticationRequest;
import com.example.ambutracker.dto.AuthenticationResponse;
import com.example.ambutracker.dto.RegisterRequest;

public interface AuthenticationService {
    AuthenticationResponse register(RegisterRequest request);
    AuthenticationResponse authenticate(AuthenticationRequest request);
} 