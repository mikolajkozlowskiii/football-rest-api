package com.example.football_api.dto.football.response;

import com.example.football_api.dto.football.request.TeamRequest;
import com.example.football_api.entities.football.League;
import com.example.football_api.entities.football.Team;
import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

@Builder
@ToString
@Getter
public class MatchResponse {
    private Long id;
    private TeamResponse homeTeam;
    private TeamResponse awayTeam;
    private LeagueResponse league;
    private int homeTeamScore;
    private int awayTeamScore;
    private LocalDate date;
    private LocalTime time;
}
