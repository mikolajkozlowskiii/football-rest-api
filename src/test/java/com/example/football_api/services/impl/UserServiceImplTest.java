package com.example.football_api.services.impl;



import com.example.football_api.dto.users.request.UpdateUserRequest;
import com.example.football_api.entities.users.AuthProvider;
import com.example.football_api.entities.users.ERole;
import com.example.football_api.entities.users.Role;
import com.example.football_api.entities.users.User;
import com.example.football_api.dto.users.response.UserResponse;
import com.example.football_api.exceptions.AppException;
import com.example.football_api.exceptions.UnauthorizedException;
import com.example.football_api.exceptions.users.UserNotFoundException;
import com.example.football_api.repositories.users.UserRepository;
import com.example.football_api.security.userDetails.UserDetailsImpl;
import com.example.football_api.services.users.RoleService;
import com.example.football_api.services.users.impl.UserServiceImpl;
import com.example.football_api.services.users.mappers.UserMapper;
import io.jsonwebtoken.lang.Assert;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;


@ExtendWith(MockitoExtension.class)
@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
class UserServiceImplTest {
    @InjectMocks
    private UserServiceImpl userService;
    @Mock
    private UserMapper userMapper;
    @Mock
    private UserRepository userRepository;
    @Mock
    private RoleService roleService;
    private User user;
    private UserDetailsImpl userDetails;
    @BeforeEach
    public void setUp() {
        Set<Role> roles = new HashSet<>(Set.of(new Role(ERole.ROLE_USER)));
        user = User.builder()
                .id(1L)
                .firstName("Mikolaj")
                .lastName("Kozlowski")
                .email("mikolaj@example.com")
                .password("passwd")
                .roles(roles)
                .isEnabled(true)
                .provider(AuthProvider.local)
                .build();

        userDetails = UserDetailsImpl.build(user);
    }

    @Test
    @DisplayName("getCurrentUser returns UserResponse with correct data")
    public void findCurrentUserResponse_CurrentUser_ReturnsUserResponse() {
        UserResponse expectedResponse = new UserResponse(user.getEmail(), user.getFirstName(),user.getLastName());

        when(userMapper.map(userDetails)).thenReturn(
                UserResponse.builder()
                .firstName(userDetails.getFirstName())
                .lastName(userDetails.getLastName())
                .email(userDetails.getEmail())
                .build()
        );
        UserResponse actualResponse = userService.findCurrentUserResponse(userDetails);

        Assertions.assertEquals(expectedResponse, actualResponse);
    }

    @Test
    void findCurrentUser_UserDetailsIsNull_ThrowsIllegalArgumentException() {
        Assertions.assertThrows(IllegalArgumentException.class,
                () -> userService.findCurrentUserResponse(null));
    }

    @Test
    public void findUserResponseByEmail_EmailFounded_ReturnsUserResponse() {
        when(userRepository.findByEmail(anyString())).thenReturn(Optional.of(user));
        when(userMapper.map(user)).thenReturn(new UserResponse(user.getEmail(), user.getFirstName(), user.getLastName()));

        UserResponse expectedResponse = new UserResponse(user.getEmail(), user.getFirstName(), user.getLastName());
        UserResponse actualResponse = userService.findUserResponseByEmail(user.getEmail());

        Assertions.assertEquals(expectedResponse, actualResponse);
    }

    @Test
    public void findUserByEmail_EmailNotFounded_ThrowsUserNotFoundException() {
        String emailNotFoundInRepo = "Email not found in repo";
        when(userRepository.findByEmail(emailNotFoundInRepo)).thenReturn(Optional.empty());

        Assertions.assertThrows(UserNotFoundException.class, () -> userService.findUserResponseByEmail(emailNotFoundInRepo));
    }

    @Test
    void updateUser_AccountBelongsToCurrentUser_AccountUpdated() {
        UpdateUserRequest updateRequest = UpdateUserRequest.builder()
                .firstName("Mikolaj")
                .lastName("Kozlowski")
                .build();

        User expectedUpdatedUser = User.builder()
                .firstName(updateRequest.getFirstName())
                .lastName(updateRequest.getLastName())
                .email(user.getEmail())
                .build();

        when(userRepository.findByEmail(user.getEmail())).thenReturn(Optional.of(user));

        when(userMapper.map(user, updateRequest)).thenReturn(
                User.builder()
                        .firstName(updateRequest.getFirstName())
                        .lastName(updateRequest.getLastName())
                        .email(user.getEmail())
                        .build()
        );

        when(userRepository.save(any(User.class))).thenReturn(
                User.builder()
                .firstName(updateRequest.getFirstName())
                .lastName(updateRequest.getLastName())
                .email(user.getEmail())
                .build()
        );

        User actualUpdatedUser = userService.updateUser(updateRequest, userDetails.getEmail(), userDetails);

        Assertions.assertEquals(expectedUpdatedUser, actualUpdatedUser);
    }

    @Test
    @Disabled
    void getUpdatedUserResponse_AccountBelongsToCurrentUser_AccountUpdated() {
    }

    @Test
    void updateUser_AccountDoesntBelongToCurrentUser_ThrownUnauthorizedException() {
        UpdateUserRequest updateRequest = UpdateUserRequest.builder()
                .firstName("Mikolaj")
                .lastName("Kozlowski")
                .build();

        Long diffrentId = user.getId() + 1L;
        User diffrentUser = User.builder()
                .id(diffrentId)
                .build();
        when(userRepository.findByEmail(user.getEmail())).thenReturn(Optional.of(diffrentUser));

        Assertions.assertThrows(UnauthorizedException.class,
                () -> userService.updateUser(updateRequest, user.getEmail(), userDetails));
    }


    @Test
    void deleteUser_AccountBelongsToCurrentUser_AccountDeleted() {
        when(userRepository.findByEmail(user.getEmail())).thenReturn(Optional.of(user));
        boolean actualSuccess = userService.deleteUser(user.getEmail(), userDetails);

        Assert.isTrue(actualSuccess);
    }

    @Test
    void deleteUser_AccountDoesntBelongToCurrentUserButUserIsAdmin_AccountDeleted() {
        when(userRepository.findByEmail(user.getEmail())).thenReturn(Optional.of(user));
        Set<Role> diffrentUserRoles = Set.of(new Role(ERole.ROLE_ADMIN));
        Long diffrentId = user.getId()+2L;
        User diffrentUser = User.builder()
                .id(diffrentId)
                .firstName("Mikolaj")
                .lastName("Messi")
                .email("example@gmail2.com")
                .isEnabled(true)
                .password("password")
                .provider(AuthProvider.local)
                .roles(diffrentUserRoles)
                .build();
        UserDetailsImpl diffrentUserDetails = UserDetailsImpl.build(diffrentUser);

        boolean actualSuccess = userService.deleteUser(user.getEmail(), diffrentUserDetails);
        Assert.isTrue(actualSuccess);
    }

    @Test
    void deleteUser_AccountDoesntBelongToCurrentUserAndUserIsNotAdmin_UnauthorizedExceptionThrown() {
        when(userRepository.findByEmail(user.getEmail())).thenReturn(Optional.of(user));
        Set<Role> diffrentUserRoles = Set.of(new Role(ERole.ROLE_USER));
        Long diffrentId = user.getId()+2L;
        User diffrentUser = User.builder()
                .id(diffrentId)
                .firstName("Mikolaj")
                .lastName("Messi")
                .email("example@gmail2.com")
                .isEnabled(true)
                .password("password")
                .provider(AuthProvider.local)
                .roles(diffrentUserRoles)
                .build();
        UserDetailsImpl diffrentUserDetails = UserDetailsImpl.build(diffrentUser);

        Assertions.assertThrows(UnauthorizedException.class, () ->  userService.deleteUser(user.getEmail(), diffrentUserDetails));
    }

    @Test
    void deleteUser_UserDoesntExistsInDB_ThrownUserNotFoundException() {
        when(userRepository.findByEmail(user.getEmail())).thenReturn(Optional.empty());

        Assertions.assertThrows(UserNotFoundException.class, () -> userService.deleteUser(user.getEmail(), userDetails));
    }

    @Test
    void giveModerator_userHasntGotAlreadyMod_ModeratorRoleGivenToUser() {
        final String usersEmail = user.getEmail();
        Role roleModerator = new Role(ERole.ROLE_MODERATOR);
        User expectedSavedUser = user;
        expectedSavedUser.getRoles().add(roleModerator);
        when(userRepository.findByEmail(usersEmail)).thenReturn(Optional.of(user));
        when(roleService.getRole(ERole.ROLE_MODERATOR)).thenReturn(roleModerator);
        when(roleService.checkIfUserHasRole(user, roleModerator)).thenReturn(false);
        when(userRepository.save(expectedSavedUser)).thenReturn(expectedSavedUser);

        userService.giveModerator(usersEmail);

        verify(userRepository).save(expectedSavedUser);
    }

    @Test
    void giveModerator_userHasGotAlreadyMod_ThrownAppException() {
        final String usersEmail = user.getEmail();
        Role roleModerator = new Role(ERole.ROLE_MODERATOR);
        User expectedSavedUser = user;
        expectedSavedUser.getRoles().add(roleModerator);
        when(userRepository.findByEmail(usersEmail)).thenReturn(Optional.of(user));
        when(roleService.getRole(ERole.ROLE_MODERATOR)).thenReturn(roleModerator);
        when(roleService.checkIfUserHasRole(user, roleModerator)).thenReturn(true);

        Assertions.assertThrows(AppException.class, () -> userService.giveModerator(usersEmail));
    }

    @Test
    void removeModerator_userHasGotAlreadyMod_ModeratorRoleGivenToUser() {
        final String usersEmail = user.getEmail();
        Role roleModerator = new Role(ERole.ROLE_MODERATOR);
        User expectedSavedUser = user;
        expectedSavedUser.getRoles().remove(roleModerator);
        when(userRepository.findByEmail(usersEmail)).thenReturn(Optional.of(user));
        when(roleService.getRole(ERole.ROLE_MODERATOR)).thenReturn(roleModerator);
        when(roleService.checkIfUserHasRole(user, roleModerator)).thenReturn(true);
        when(userRepository.save(expectedSavedUser)).thenReturn(expectedSavedUser);

        userService.removeModerator(usersEmail);

        verify(userRepository).save(expectedSavedUser);
    }

    @Test
    void removeModerator_userHasntGotAlreadyMod_ThrownAppException() {
        final String usersEmail = user.getEmail();
        Role roleModerator = new Role(ERole.ROLE_MODERATOR);
        when(userRepository.findByEmail(usersEmail)).thenReturn(Optional.of(user));
        when(roleService.getRole(ERole.ROLE_MODERATOR)).thenReturn(roleModerator);
        when(roleService.checkIfUserHasRole(user, roleModerator)).thenReturn(false);

        Assertions.assertThrows(AppException.class, () -> userService.removeModerator(usersEmail));
    }

    @Test
    void removeModerator() {
        final String usersEmail = user.getEmail();
        Role roleModerator = new Role(ERole.ROLE_MODERATOR);
        User expectedSavedUser = user;
        expectedSavedUser.getRoles().remove(roleModerator);
        when(userRepository.findByEmail(usersEmail)).thenReturn(Optional.of(user));
        when(roleService.getRole(ERole.ROLE_MODERATOR)).thenReturn(roleModerator);
        when(roleService.checkIfUserHasRole(user, roleModerator)).thenReturn(true);
        when(userRepository.save(expectedSavedUser)).thenReturn(expectedSavedUser);

        userService.removeModerator(usersEmail);

        verify(userRepository).save(expectedSavedUser);
    }


    @Test
    void findUserByEmail_EmailBelongsToUser_ReturnsUser() {
        final User expectedUser = user;
        final String usersEmail = user.getEmail();
        when(userRepository.findByEmail(usersEmail)).thenReturn(Optional.of(user));

        final User actualUser = userService.findUserByEmail(usersEmail);

        Assertions.assertEquals(expectedUser, actualUser);
    }

    @Test
    void findUserByEmail_EmailDoesntBelongToAnyUser_ThrownUserNotFoundException() {
        final String emailNotFoundInRepo = user.getEmail();
        when(userRepository.findByEmail(emailNotFoundInRepo)).thenReturn(Optional.empty());

        Assertions.assertThrows(UserNotFoundException.class, () -> userService.findUserByEmail(emailNotFoundInRepo));
    }

    @Test
    void findUserById() {
        final User expectedUser = user;
        final Long userId = user.getId();
        when(userRepository.findById(userId)).thenReturn(Optional.of(user));

        final User actualUser = userService.findUserById(userId);

        Assertions.assertEquals(expectedUser, actualUser);
    }

    @Test
    void findUserByEmail22() {
        final Long userIdNotInRepo = 123L;
        when(userRepository.findById(userIdNotInRepo)).thenReturn(Optional.empty());

        Assertions.assertThrows(UserNotFoundException.class, () -> userService.findUserById(userIdNotInRepo));
    }
}