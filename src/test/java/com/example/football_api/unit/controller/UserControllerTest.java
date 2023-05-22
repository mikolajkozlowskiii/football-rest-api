package com.example.football_api.unit.controller;

import com.example.football_api.controllers.users.UserController;
import com.example.football_api.dto.users.response.UserResponse;
import com.example.football_api.exceptions.GlobalExceptionHandler;
import com.example.football_api.security.jwt.JwtUtils;
import com.example.football_api.security.userDetails.UserDetailsImpl;
import com.example.football_api.services.users.RoleService;
import com.example.football_api.services.users.UserService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.autoconfigure.json.AutoConfigureJsonTesters;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;


/*
@WebMvcTest(UserController.class)

public class UserControllerTest {
    private MockMvc mvc;
    @InjectMocks
    private UserController userController;
    @Mock
    private UserService userService;
    @Mock
    private RoleService roleService;
    private JacksonTester<UserResponse> jsonUserResponse;

    @BeforeEach
    public void setup() {
        JacksonTester.initFields(this, new ObjectMapper());
        mvc = MockMvcBuilders.standaloneSetup(userController)
                .setControllerAdvice(new GlobalExceptionHandler())
                //.addFilters()
                .build();
    }

    @Test
    @WithMockUser(roles = "ROLE_USER")
    public void getCurrentUser() throws Exception{
        UserResponse userResponse = UserResponse.builder()
                .id(1L)
                .firstName("Mikolaj")
                .lastName("Kozlowski")
                .email("mikolajkozlowskiii@gmail.com")
                .build();
        when(userService.findCurrentUserResponse(any())).thenReturn(userResponse);

        MockHttpServletResponse response = mvc.perform(get("/api/v1/users")
                        .accept(MediaType.APPLICATION_JSON)).andReturn().getResponse();

        Assertions.assertThat(response.getStatus()).isEqualTo(HttpStatus.OK.value());
        Assertions.assertThat(response.getContentAsString()).isEqualTo(
                jsonUserResponse.write(UserResponse.builder()
                                .firstName("Mikolaj")
                                .lastName("Kozlowski")
                                .email("mikolajkozlowskiii@gmail.com")
                                .build()).getJson()
        );
    }
}
*/