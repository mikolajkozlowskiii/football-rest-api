package com.example.football_api.unit.controller;

import com.example.football_api.controllers.users.AuthController;
import com.example.football_api.controllers.users.UserController;
import com.example.football_api.dto.users.request.LoginRequest;
import com.example.football_api.dto.users.response.JwtResponse;
import com.example.football_api.dto.users.response.UserResponse;
import com.example.football_api.email.services.ConfirmationTokenServiceImpl;
import com.example.football_api.entities.users.ERole;
import com.example.football_api.entities.users.Role;
import com.example.football_api.exceptions.GlobalExceptionHandler;
import com.example.football_api.security.jwt.JwtUtils;
import com.example.football_api.services.users.AuthService;
import com.example.football_api.services.users.RoleService;
import com.example.football_api.services.users.UserService;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mysql.cj.log.Log;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(SpringExtension.class)
@WebMvcTest(controllers = AuthController.class)
@AutoConfigureMockMvc
public class AuhtControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;
    @MockBean
    private AuthService authService;
    @MockBean
    private ConfirmationTokenServiceImpl confirmationTokenService;
    @Autowired
    private GlobalExceptionHandler globalExceptionHandler;
    @Autowired
    private LocalValidatorFactoryBean validator;

    @Test
    @WithUserDetails("manager@company.com")
    public void test() throws Exception{
        LoginRequest loginRequest = LoginRequest.builder()
                .email("email@test.pl")
                .password("password123")
                .build();
        JwtResponse jwtResponse = JwtResponse.builder().token("tokenJWT").id(1L).email("test@gmail.com").roles(List.of("ROLE")).build();
        when(authService.signIn(any())).thenReturn(jwtResponse);


        mockMvc.perform(post("/api/v1/auth/singin")
                .contentType("application/json").content(objectMapper.writeValueAsString(loginRequest)))
                .andExpect(status().isOk());
    }
    /*
    @BeforeEach
    public void setup() {
        JacksonTester.initFields(this, new ObjectMapper());
        mvc = MockMvcBuilders.standaloneSetup(authController)
                .setControllerAdvice(new GlobalExceptionHandler())
                .build();
    }

    @Test
    public void signin() throws Exception{
        JwtResponse jwtResponse = JwtResponse.builder()
                .id(1L)
                .email("test@gmail.com")
                .token("jwt token")
                .build();

        when(authService.signIn(any())).thenReturn(
                JwtResponse.builder()
                        .id(1L)
                        .email("test@gmail.com")
                        .token("jwt token")
                        .build()
        );

        MockHttpServletResponse response = mvc.perform(post("/api/v1/auth/singin")
                .contentType(MediaType.APPLICATION_JSON).content(
                jsonLoginRequest.write(LoginRequest.builder()
                                .email("email@test.pl")
                                .password("password123")
                                .build()).getJson())).andReturn().getResponse();


        System.out.println("tutaj: "+response.getContentAsString().length());
        System.out.println(response);
       // assertEquals(jsonJwtResponse.write(jwtResponse), response.getContentAsString());
        assertEquals(HttpStatus.OK.value(), response.getStatus());

    }
    */

}
