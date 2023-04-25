package com.example.football_api.services;


import com.example.football_api.models.User;
import com.example.football_api.payload.request.UpdateUserRequest;
import com.example.football_api.payload.response.ApiResponse;
import com.example.football_api.payload.response.UserResponse;
import com.example.football_api.security.userDetails.UserDetailsImpl;

public interface UserService {
    UserResponse getCurrentUser(UserDetailsImpl currentUser);
    UserResponse getUserByEmail(String email);
    UserResponse updateUser(UpdateUserRequest updatedUser, String username, UserDetailsImpl currentUser);
    boolean deleteUser(String username, UserDetailsImpl currentUser);
    ApiResponse giveModerator(String username);
    ApiResponse removeModerator(String username);
    User findUserByEmail(String email);
    User findUserById(Long id);
}
