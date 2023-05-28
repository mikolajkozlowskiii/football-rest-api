package com.example.football_api.entities.football;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.*;
import net.jcip.annotations.Immutable;

@Entity(name = "league_table_view")
@Immutable
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Builder
@Getter
public class LeagueTableView {
    @Id
    private Long id;
    private Long leagueId;
    private String teamName;
    private int playedMatches;
    private int wins;
    private int draws;
    private int losses;
    private int goalsFor;
    private int goalsAgainst;
    private int goalDifference;
    private int points;
}