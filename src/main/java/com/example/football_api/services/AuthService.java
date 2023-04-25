package com.example.football_api.services;


import com.example.football_api.models.User;
import com.example.football_api.payload.request.LoginRequest;
import com.example.football_api.payload.request.SignUpRequest;
import com.example.football_api.payload.response.JwtResponse;

public interface AuthService {
    User createUser(SignUpRequest request);
    JwtResponse signIn(LoginRequest request);
    boolean checkEmailAvailability(String email);
    int enableUser(String email);
}
