package com.example.football_api.dto.football.response.overview;

import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Set;

@Builder
@ToString
@Getter
public class MatchOverview {
    private Long matchId;
    private LocalDate date;
    private LocalTime time;
    private int homeTeamScore;
    private int awayTeamScore;
    private LeagueOverview league;
    private TeamOverview homeTeam;
    private TeamOverview awayTeam;
    private Set<GoalOverview> goals;

}
