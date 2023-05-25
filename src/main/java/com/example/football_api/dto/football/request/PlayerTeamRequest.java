package com.example.football_api.dto.football.request;

import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDate;

@Builder
@ToString
@Getter
@Setter
public class PlayerTeamRequest {
    @NotNull
    private Long teamId;
    private LocalDate start;
    private LocalDate ends;
}
