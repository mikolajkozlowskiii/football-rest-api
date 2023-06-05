package com.example.football_api.dto.football.response;

import com.example.football_api.dto.football.response.overview.GoalOverview;
import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

@Builder
@ToString
@Getter
public class MatchOverviewResponse {
    private Long id;
    private TeamResponse homeTeam;
    private TeamResponse awayTeam;
    private LeagueResponse league;
    private int homeTeamScore;
    private int awayTeamScore;
    private LocalDate date;
    private LocalTime time;
}
