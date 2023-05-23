package com.example.football_api.dto.football.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

import java.util.Date;

@Builder
@ToString
@Getter
public class PlayerResponse {
    private Long id;
    private Long teamId;
    private String firstName;
    private String lastName;
    private String position;
    private int height;
    private int weight;
    private boolean strongerFeet;
    private Date birthDate;
}
