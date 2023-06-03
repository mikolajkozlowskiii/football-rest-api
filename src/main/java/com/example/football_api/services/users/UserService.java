package com.example.football_api.services.users;


import com.example.football_api.entities.users.User;
import com.example.football_api.dto.users.request.UpdateUserRequest;
import com.example.football_api.dto.users.response.ApiResponse;
import com.example.football_api.dto.users.response.UserResponse;
import com.example.football_api.security.userDetails.UserDetailsImpl;

import java.util.List;

public interface UserService {
    UserResponse findCurrentUserResponse(UserDetailsImpl currentUser);
    UserResponse findUserResponseByEmail(String email);
    User updateUser(UpdateUserRequest updatedUser, String username, UserDetailsImpl currentUser);
    UserResponse getUpdatedUserResponse(UpdateUserRequest updateInfoRequest, String email, UserDetailsImpl currentUser);
    boolean deleteUser(String username, UserDetailsImpl currentUser);
    ApiResponse giveModerator(String username);
    ApiResponse removeModerator(String username);
    User findUserByEmail(String email);
    User findUserById(Long id);
    List<User> findAllUsersWithOnlyUserRole();
    List<User> findAllUsersWithOnlyModeratorRoleAndNotAdmin();
    List<UserResponse> findAllUsersResponseWithOnlyUserRole();
    List<UserResponse> findAllUsersResponseWithModeratorRoleAndNotAdmin();
}
