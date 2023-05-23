package com.example.football_api.dto.football.request;

import com.example.football_api.entities.football.Team;
import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.validator.constraints.Length;

import java.util.Date;

@Builder
@ToString
@Getter
@Setter
public class PlayerRequest {
    @NotNull
    private Long teamId;
    @NotBlank
    @Size(min = 2, max = 30)
    private String firstName;
    @Size(min = 2, max = 30)
    private String lastName;
    @Length(min = 2, max = 3)
    private String position;
    @Max(250)
    @Min(140)
    private int height;
    @Max(250)
    @Min(50)
    private int weight;
    private boolean strongerFeet;
    @JsonFormat(pattern="yyyy-MM-dd")
    private Date birthDate;
}
