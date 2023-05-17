package com.example.football_api.dto.football.validation;


import com.example.football_api.exceptions.validators.ValidateMatchTeams;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDateTime;

@Builder
@ToString
@Getter
@Setter
@ValidateMatchTeams(
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
    private LocalDateTime dateTime;
}
