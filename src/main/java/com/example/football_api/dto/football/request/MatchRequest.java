package com.example.football_api.dto.football.request;


import com.example.football_api.exceptions.validators.MatchConstraints;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

@Builder
@ToString
@Getter
@Setter
@MatchConstraints(
        homeTeam = "homeTeamId",
        awayTeam = "awayTeamId",
        message = "homeTeamId can't be equal as awayTeamId"
)
public class MatchRequest {
    @NotNull
    private Long homeTeamId;
    @NotNull
    private Long awayTeamId;
    @NotNull
    private Long leagueId;
    private int homeTeamScore;
    private int awayTeamScore;
    @NotNull
    private LocalDate date;
    private LocalTime time;
}
