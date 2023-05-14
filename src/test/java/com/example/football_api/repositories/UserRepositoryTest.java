package com.example.football_api.repositories;

import com.example.football_api.entities.users.AuthProvider;
import com.example.football_api.entities.users.ERole;
import com.example.football_api.entities.users.Role;
import com.example.football_api.entities.users.User;
import com.example.football_api.exceptions.users.UserNotFoundException;
import com.example.football_api.repositories.users.UserRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@DataJpaTest()
public class UserRepositoryTest {
    @Autowired
    private UserRepository userRepository;
    private User firstUser;
    private User secondUser;

    @BeforeEach
    public void setUp(){
        firstUser = User.builder()
                .firstName("Mikolaj")
                .lastName("Kozlowski")
                .email("mikolajkozlowskiii@gmail.com")
                .roles(Set.of(new Role(ERole.ROLE_USER)))
                .isEnabled(true)
                .provider(AuthProvider.local)
                .password("passwd")
                .build();

        secondUser = User.builder()
                .firstName("Walter")
                .lastName("White")
                .email("breaking@bad.net")
                .roles(Set.of(new Role(ERole.ROLE_USER)))
                .isEnabled(true)
                .provider(AuthProvider.local)
                .password("passwd")
                .build();
    }


    @Test
    public void save_UserInstace_UserSaved(){
        final User user = firstUser;

        final User savedUser = userRepository.save(user);

        Assertions.assertNotNull(savedUser);
        Assertions.assertTrue(savedUser.getId() > 0L);
    }

    @Test
    public void save_ListOfUsers_UsersSaved(){
        final User firstUser = this.firstUser;
        final User secondUser = this.secondUser;

        final List<User> expectedUsers = List.of(firstUser, secondUser);
        userRepository.saveAll(new ArrayList<>(expectedUsers));

        final List<User> actualUsers = userRepository.findAll();

        Assertions.assertIterableEquals(expectedUsers, actualUsers);
    }

    @Test
    public void findById_UsersIdBelongsToCurrentUser_UserFound(){
        final User firstUser = this.firstUser;
        final User secondUser = this.secondUser;
        final List<User> expectedUsers = List.of(firstUser, secondUser);
        userRepository.saveAll(new ArrayList<>(expectedUsers));

        final Long firstUserId = firstUser.getId();
        final User actualUser = userRepository
                .findById(firstUserId)
                .orElseThrow(() -> new UserNotFoundException(firstUserId.toString()));


        Assertions.assertEquals(firstUser, actualUser);
    }

    @Test
    public void findById_UsersIdDoesntBelongsToAnyUser_ThrownUserNotFound(){
        final User firstUser = this.firstUser;
        final User secondUser = this.secondUser;
        final List<User> expectedUsers = List.of(firstUser, secondUser);
        userRepository.saveAll(new ArrayList<>(expectedUsers));

        final Long diffrentId = secondUser.getId() + firstUser.getId();

        Assertions.assertThrows(UserNotFoundException.class, () -> userRepository
                .findById(diffrentId)
                .orElseThrow(() -> new UserNotFoundException(diffrentId.toString())));
    }

    @Test
    public void findByEmail_UsersEmailBelongsToCurrentUser_UserFound(){
        final User firstUser = this.firstUser;
        final User secondUser = this.secondUser;
        final List<User> expectedUsers = List.of(firstUser, secondUser);
        userRepository.saveAll(new ArrayList<>(expectedUsers));

        final String firstUserEmail = firstUser.getEmail();
        final User actualUser = userRepository
                .findByEmail(firstUserEmail)
                .orElseThrow(() -> new UserNotFoundException(firstUserEmail));


        Assertions.assertEquals(firstUser, actualUser);
    }

    @Test
    public void findByEmail_UsersEmailDoesntBelongsToAnyUser_ThrownUserNotFound(){
        final User firstUser = this.firstUser;
        final User secondUser = this.secondUser;
        final List<User> expectedUsers = List.of(firstUser, secondUser);
        userRepository.saveAll(new ArrayList<>(expectedUsers));

        final String diffrentEmail = firstUser.getEmail() + secondUser.getEmail();

        Assertions.assertThrows(UserNotFoundException.class, () -> userRepository
                .findByEmail(diffrentEmail)
                .orElseThrow(() -> new UserNotFoundException(diffrentEmail)));
    }

    @Test
    public void existsByEmail_UsersExistsInDB_UserFound(){
        final User firstUser = this.firstUser;
        final User secondUser = this.secondUser;
        final List<User> expectedUsers = List.of(firstUser, secondUser);
        userRepository.saveAll(new ArrayList<>(expectedUsers));

        final String firstUserEmail = firstUser.getEmail();
        final boolean actualExists = userRepository.existsByEmail(firstUserEmail);

        Assertions.assertTrue(actualExists);
    }

    @Test
    public void existsByEmail_UsersDoesntExistsInDB_UserFound(){
        final User firstUser = this.firstUser;
        final User secondUser = this.secondUser;
        final List<User> expectedUsers = List.of(firstUser, secondUser);
        userRepository.saveAll(new ArrayList<>(expectedUsers));

        final String firstUserEmail = firstUser.getEmail() + secondUser.getEmail();
        final boolean actualExists = userRepository.existsByEmail(firstUserEmail);

        Assertions.assertFalse(actualExists);
    }

    @Test
    public void remove_UserExistsInDB_RemovedUser(){
        final User firstUser = this.firstUser;
        final User secondUser = this.secondUser;
        final List<User> expectedUsers = List.of(firstUser, secondUser);
        userRepository.saveAll(new ArrayList<>(expectedUsers));

        userRepository.delete(firstUser);
        Optional<User> expectedUser = Optional.empty();
        Optional<User> actualUser = userRepository.findById(firstUser.getId());

        Assertions.assertEquals(expectedUser, actualUser);
    }

    @Test
    @Disabled("Not implemented yet")
    public void enableAppUser_UsersExistsAndDisabled_UserEnabled(){
    }

    @Test
    @Disabled("Not implemented yet")
    public void enableAppUser_UsersExistsAndEnabled_UserEnabledStill(){
    }




}
