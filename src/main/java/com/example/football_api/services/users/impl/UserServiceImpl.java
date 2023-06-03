package com.example.football_api.services.users.impl;

import com.example.football_api.exceptions.AppException;
import com.example.football_api.exceptions.UnauthorizedException;
import com.example.football_api.entities.users.ERole;
import com.example.football_api.entities.users.Role;
import com.example.football_api.entities.users.User;
import com.example.football_api.dto.users.request.UpdateUserRequest;
import com.example.football_api.dto.users.response.ApiResponse;
import com.example.football_api.dto.users.response.UserResponse;
import com.example.football_api.exceptions.users.UserNotFoundException;
import com.example.football_api.repositories.users.UserRepository;
import com.example.football_api.security.userDetails.UserDetailsImpl;
import com.example.football_api.services.users.RoleService;
import com.example.football_api.services.users.UserService;
import com.example.football_api.services.users.mappers.UserMapper;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserMapper userMapper;
    private final UserRepository userRepository;
    private final RoleService roleService;

    @Override
    public UserResponse findCurrentUserResponse(UserDetailsImpl userDetails) {
        if(Objects.isNull(userDetails)){
            throw new IllegalArgumentException("UserDetails instance can't be null");
        }
        return userMapper.map(userDetails);
    }

    @Override
    public UserResponse findUserResponseByEmail(String email) {
        return userMapper.map(findUserByEmail(email));
    }

    @Override
    public User updateUser(UpdateUserRequest updateInfoRequest, String email, UserDetailsImpl currentUser) {
        User user = findUserByEmail(email);
        if(user.getId().equals(currentUser.getId())){
            User updatedUser = userMapper.map(user, updateInfoRequest);
            return save(updatedUser);
        }
        throw new UnauthorizedException("Can't update not your account.");
    }

    @Override
    public UserResponse getUpdatedUserResponse(UpdateUserRequest updateInfoRequest, String email, UserDetailsImpl currentUser) {
        User updatedUser = updateUser(updateInfoRequest, email, currentUser);
        return userMapper.map(updatedUser);
    }

    @Override
    @Transactional
    public boolean deleteUser(String email, UserDetailsImpl currentUser) {
        User user = findUserByEmail(email);
        checkIfCurrentUserCanDeleteAccount(currentUser, user);
        delete(user);
        return true;
    }

    private static void checkIfCurrentUserCanDeleteAccount(UserDetailsImpl currentUser, User account) {
        if(!account.getId().equals(currentUser.getId()) &&
                !currentUser.getAuthorities().contains(new SimpleGrantedAuthority(ERole.ROLE_ADMIN.name()))){
            throw new UnauthorizedException("Can't delete not your account.");
        }
    }

    public void delete(User user) {
        userRepository.delete(user);
    }

    @Override
    public ApiResponse giveModerator(String email) {
        User user = findUserByEmail(email);

        Role roleModerator = roleService.getRole(ERole.ROLE_MODERATOR);
        if(roleService.checkIfUserHasRole(user, roleModerator)){
            throw new AppException(email + " has already role " + ERole.ROLE_MODERATOR.name());
        }
        user.getRoles().add(roleModerator);
        save(user);

        return new ApiResponse(Boolean.TRUE, "Moderator role set to user: " + email);
    }

    public User save(User user) {
        return userRepository.save(user);
    }

    @Override
    public ApiResponse removeModerator(String email) {
        User user = findUserByEmail(email);

        Role roleModerator = roleService.getRole(ERole.ROLE_MODERATOR);
        if(!roleService.checkIfUserHasRole(user, roleModerator)){
            throw new AppException(email + " hasn't got role " + ERole.ROLE_MODERATOR.name());
        }

        user.getRoles().remove(roleModerator);
        save(user);

        return new ApiResponse(Boolean.TRUE, "Moderator role removed from user: " + email);
    }

    @Override
    public User findUserByEmail(String email) {
        return userRepository
                .findByEmail(email)
                .orElseThrow(() -> new UserNotFoundException(email));
    }

    @Override
    public User findUserById(Long id) {
        return userRepository
                .findById(id)
                .orElseThrow(() -> new UserNotFoundException(String.valueOf(id)));
    }

    @Override
    public List<User> findAllUsersWithOnlyUserRole() {
        return userRepository
                .findAll()
                .stream()
                .filter(
                        s-> !s.getRoles().contains(new Role(ERole.ROLE_MODERATOR))
                                && !s.getRoles().contains(new Role(ERole.ROLE_ADMIN)))
                .toList();
    }

    @Override
    public List<User> findAllUsersWithOnlyModeratorRoleAndNotAdmin() {
        return userRepository
                .findAll()
                .stream()
                .filter(
                        s-> s.getRoles().contains(new Role(ERole.ROLE_MODERATOR))
                                && !s.getRoles().contains(new Role(ERole.ROLE_ADMIN)))
                .toList();
    }

    @Override
    public List<UserResponse> findAllUsersResponseWithOnlyUserRole() {
        return findAllUsersWithOnlyUserRole()
                .stream()
                .map(userMapper::map)
                .toList();
    }

    @Override
    public List<UserResponse> findAllUsersResponseWithModeratorRoleAndNotAdmin() {
        return findAllUsersWithOnlyModeratorRoleAndNotAdmin()
                .stream()
                .map(userMapper::map)
                .toList();
    }
}