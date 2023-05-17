package com.example.football_api.unit.validation;

import com.example.football_api.dto.football.validation.MatchRequest;
import jakarta.validation.ConstraintViolation;

import jakarta.validation.Validator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;


import java.time.LocalDateTime;
import java.util.Set;

import static jakarta.validation.Validation.buildDefaultValidatorFactory;
import static org.junit.jupiter.api.Assertions.*;

class MatchRequestTest {
    private Validator validator;
    @BeforeEach
    public void setup(){
        validator = buildDefaultValidatorFactory().getValidator();
    }
    @Test
    public void validateMatchRequest_AwayTeamEqualsHomeTeam_ViolationValidateMatchTeams(){
        MatchRequest notValidMatchRequest = MatchRequest.builder()
                .awayTeamId(1L)
                .homeTeamId(1L)
                .dateTime(LocalDateTime.of(2023, 5, 17, 11, 50))
                .leagueId(5L)
                .build();

        Set<ConstraintViolation<MatchRequest>> violations = validator.validate(notValidMatchRequest);

        assertEquals(1, violations.size());
        ConstraintViolation<MatchRequest> violation = violations.iterator().next();
        assertEquals("homeTeamId can't be equal as awayTeamId", violation.getMessage());
        assertEquals("ValidateMatchTeams", violation.getConstraintDescriptor().getAnnotation().annotationType().getSimpleName());
    }
    @Test
    public void validateMatchRequest_AwayTeamNotEqualsHomeTeam_NoViolation(){
        MatchRequest notValidMatchRequest = MatchRequest.builder()
                .awayTeamId(1L)
                .homeTeamId(2L)
                .dateTime(LocalDateTime.of(2023, 5, 17, 11, 50))
                .leagueId(5L)
                .build();

        Set<ConstraintViolation<MatchRequest>> violations = validator.validate(notValidMatchRequest);

        assertEquals(0, violations.size());
    }
    @Test
    public void validateMatchRequest_AwayTeamNull_ViolationNotNull(){
        MatchRequest notValidMatchRequest = MatchRequest.builder()
                .awayTeamId(null)
                .homeTeamId(1L)
                .dateTime(LocalDateTime.of(2023, 5, 17, 11, 50))
                .leagueId(5L)
                .build();

        Set<ConstraintViolation<MatchRequest>> violations = validator.validate(notValidMatchRequest);

        assertEquals(1, violations.size());
        ConstraintViolation<MatchRequest> violation = violations.iterator().next();
        assertEquals("NotNull", violation.getConstraintDescriptor().getAnnotation().annotationType().getSimpleName());
    }

    @Test
    public void validateMatchRequest_HomeTeamNull_ViolationNotNull(){
        MatchRequest notValidMatchRequest = MatchRequest.builder()
                .awayTeamId(1L)
                .homeTeamId(null)
                .dateTime(LocalDateTime.of(2023, 5, 17, 11, 50))
                .leagueId(5L)
                .build();

        Set<ConstraintViolation<MatchRequest>> violations = validator.validate(notValidMatchRequest);

        assertEquals(1, violations.size());
        ConstraintViolation<MatchRequest> violation = violations.iterator().next();
        assertEquals("NotNull", violation.getConstraintDescriptor().getAnnotation().annotationType().getSimpleName());
    }

    @Test
    public void validateMatchRequest_HomeAndAwayTeamNull_ViolationNotNull(){
        MatchRequest notValidMatchRequest = MatchRequest.builder()
                .awayTeamId(null)
                .homeTeamId(null)
                .dateTime(LocalDateTime.of(2023, 5, 17, 11, 50))
                .leagueId(5L)
                .build();

        Set<ConstraintViolation<MatchRequest>> violations = validator.validate(notValidMatchRequest);

        assertEquals(2, violations.size());
        ConstraintViolation<MatchRequest> violation = violations.iterator().next();
        assertEquals("NotNull", violation.getConstraintDescriptor().getAnnotation().annotationType().getSimpleName());
    }

    @Test
    public void validateMatchRequest_LeagueIdNull_ViolationNotNull(){
        MatchRequest notValidMatchRequest = MatchRequest.builder()
                .awayTeamId(1L)
                .homeTeamId(2L)
                .dateTime(LocalDateTime.of(2023, 5, 17, 11, 50))
                .leagueId(null)
                .build();

        Set<ConstraintViolation<MatchRequest>> violations = validator.validate(notValidMatchRequest);

        assertEquals(1, violations.size());
        ConstraintViolation<MatchRequest> violation = violations.iterator().next();
        assertEquals("NotNull", violation.getConstraintDescriptor().getAnnotation().annotationType().getSimpleName());
    }

    @Test
    public void validateMatchRequest_DateTimeNull_ViolationNotNull(){
        MatchRequest notValidMatchRequest = MatchRequest.builder()
                .awayTeamId(1L)
                .homeTeamId(2L)
                .dateTime(null)
                .leagueId(5L)
                .build();

        Set<ConstraintViolation<MatchRequest>> violations = validator.validate(notValidMatchRequest);

        assertEquals(1, violations.size());
        ConstraintViolation<MatchRequest> violation = violations.iterator().next();
        assertEquals("NotNull", violation.getConstraintDescriptor().getAnnotation().annotationType().getSimpleName());
    }

    @Test
    public void validateMatchRequest_AllPropertiesNull_ViolationNotNull(){
        MatchRequest notValidMatchRequest = MatchRequest.builder()
                .awayTeamId(null)
                .homeTeamId(null)
                .dateTime(null)
                .leagueId(null)
                .build();

        Set<ConstraintViolation<MatchRequest>> violations = validator.validate(notValidMatchRequest);

        assertEquals(4, violations.size());
        ConstraintViolation<MatchRequest> violation = violations.iterator().next();
        assertEquals("NotNull", violation.getConstraintDescriptor().getAnnotation().annotationType().getSimpleName());
    }
}