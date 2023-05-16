package com.example.football_api.unit.repositories;

import com.example.football_api.entities.users.AuthProvider;
import com.example.football_api.entities.users.ERole;
import com.example.football_api.entities.users.Role;
import com.example.football_api.entities.users.User;
import com.example.football_api.exceptions.users.UserNotFoundException;
import com.example.football_api.repositories.users.UserRepository;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;


import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@DataJpaTest()
@Transactional(propagation = Propagation.NEVER)
public class UserRepositoryTest {
    @Autowired
    private UserRepository userRepository;

    @Test
    @Sql(value = "classpath:/delete-users.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    public void save_UserInstace_UserSaved(){
        final User user = User.builder()
                .firstName("Mikolaj")
                .lastName("Kozlowski")
                .email("mikolaj@gmail.com")
                .roles(Set.of(new Role(ERole.ROLE_USER)))
                .isEnabled(true)
                .provider(AuthProvider.local)
                .password("passwd")
                .build();

        final User savedUser = userRepository.save(user);

        Assertions.assertNotNull(savedUser);
    }

    @Test
    @Sql(value = "classpath:/delete-users.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    public void save_ListOfUsers_UsersSaved(){
        final User firstUser = User.builder()
                .firstName("Mikolaj")
                .lastName("Kozlowski")
                .email("mikolaj@gmail.com")
                .isEnabled(true)
                .roles(Set.of())
                .provider(AuthProvider.local)
                .password("passwd")
                .build();

        final User secondUser = User.builder()
                .firstName("Gustavo")
                .lastName("Fring")
                .email("los@gmail.com")
                .isEnabled(true)
                .roles(Set.of())
                .provider(AuthProvider.local)
                .password("passwd")
                .build();

        final List<User> expectedUsers = List.of(firstUser, secondUser);
        userRepository.saveAll(new ArrayList<>(expectedUsers));

        final List<User> actualUsers = userRepository.findAll();

        Assertions.assertIterableEquals(expectedUsers, actualUsers);
    }

    @Test
    @Sql(value = "classpath:/import-users.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(value = "classpath:/delete-users.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    public void findById_UsersIdBelongsToCurrentUser_UserFound(){
        final User expectedUser = User.builder()
                .id(1L)
                .firstName("Mikolaj")
                .lastName("Kozlowski")
                .email("mikolajkozlowskiii@gmail.com")
                .isEnabled(true)
                .roles(Set.of())
                .provider(AuthProvider.local)
                .password("password")
                .build();

         final User actualUser = userRepository
                .findById(1L)
                .orElseThrow(() -> new UserNotFoundException("mikolaj@gmail.com"));

        Assertions.assertEquals(expectedUser, actualUser);
    }

    @Test
    public void findById_UsersIdDoesntBelongsToAnyUser_ThrownUserNotFound(){
        Assertions.assertThrows(UserNotFoundException.class, () -> userRepository
                .findById(100L)
                .orElseThrow(() -> new UserNotFoundException("mikolaj@gmail.com")));
    }

    @Test
    @Sql(value = "classpath:/import-users.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(value = "classpath:/delete-users.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    public void findByEmail_UsersEmailBelongsToCurrentUser_UserFound(){
        final User expectedUser = User.builder()
                .id(1L)
                .firstName("Mikolaj")
                .lastName("Kozlowski")
                .email("mikolajkozlowskiii@gmail.com")
                .isEnabled(true)
                .roles(Set.of())
                .provider(AuthProvider.local)
                .password("password")
                .build();

        final User actualUser = userRepository
                .findByEmail("mikolajkozlowskiii@gmail.com")
                .orElseThrow(() -> new UserNotFoundException("mikolajkozlowskiii@gmail.com"));

        Assertions.assertEquals(expectedUser, actualUser);
    }

    @Test
    public void findByEmail_UsersEmailDoesntBelongsToAnyUser_ThrownUserNotFound(){
        Assertions.assertThrows(UserNotFoundException.class, () -> userRepository
                .findByEmail("email@gmail.com")
                .orElseThrow(() -> new UserNotFoundException("email@gmail.com")));
    }

    @Test
    @Sql(value = "classpath:/import-users.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(value = "classpath:/delete-users.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    public void existsByEmail_UsersExistsInDB_UserFound(){
        final User existedUserInDB = User.builder()
                .id(1L)
                .firstName("Mikolaj")
                .lastName("Kozlowski")
                .email("mikolajkozlowskiii@gmail.com")
                .isEnabled(true)
                .roles(Set.of())
                .provider(AuthProvider.local)
                .password("password")
                .build();

        final boolean actualExists = userRepository.existsByEmail("mikolajkozlowskiii@gmail.com");

        Assertions.assertTrue(actualExists);
    }

    @Test
    public void existsByEmail_UsersDoesntExistsInDB_UserFound(){
        final boolean actualExists = userRepository.existsByEmail("emailNotInDB@gmail.com");
        Assertions.assertFalse(actualExists);
    }

    @Test
    @Sql(value = "classpath:/import-users.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(value = "classpath:/delete-users.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    public void remove_UserExistsInDB_RemovedUser(){
        final User existedUserInDB = User.builder()
                .id(1L)
                .firstName("Mikolaj")
                .lastName("Kozlowski")
                .email("mikolajkozlowskiii@gmail.com")
                .isEnabled(true)
                .roles(Set.of())
                .provider(AuthProvider.local)
                .password("password")
                .build();

        userRepository.delete(existedUserInDB);

        Optional<User> expectedUser = Optional.empty();
        Optional<User> actualUser = userRepository.findById(1L);

        Assertions.assertEquals(expectedUser, actualUser);
    }

    @Test
    @Sql(value = "classpath:/import-users.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(value = "classpath:/delete-users.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    public void enableAppUser_UsersExistsAndDisabled_UserEnabled(){
         User existedUserInDB = User.builder()
                .id(2L)
                .firstName("Walter")
                .lastName("White")
                .email("heisenberg@gmail.com")
                .isEnabled(false)
                .roles(Set.of())
                .provider(AuthProvider.local)
                .password("password")
                .build();

        userRepository.enableAppUser("heisenberg@gmail.com");
        User expectedModifiedUser = userRepository
                .findById(2L)
                .orElseThrow(() -> new UserNotFoundException("heisenberg@gmail.com"));

        Assertions.assertTrue(expectedModifiedUser.isEnabled());
    }

    @Test
    @Sql(value = "classpath:/import-users.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(value = "classpath:/delete-users.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    public void enableAppUser_UsersExistsAndEnabled_UserEnabledStill(){
        User existedUserInDB = User.builder()
                .id(1L)
                .firstName("Mikolaj")
                .lastName("Kozlowski")
                .email("mikolajkozlowskiii@gmail.com")
                .isEnabled(true)
                .roles(Set.of())
                .provider(AuthProvider.local)
                .password("password")
                .build();

        userRepository.enableAppUser("heisenberg@gmail.com");
        User expectedModifiedUser = userRepository
                .findById(1L)
                .orElseThrow(() -> new UserNotFoundException("mikolajkozlowskiii@gmail.com"));

        Assertions.assertTrue(expectedModifiedUser.isEnabled());
    }


}
