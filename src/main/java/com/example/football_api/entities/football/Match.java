package com.example.football_api.entities.football;


import com.example.football_api.exceptions.validators.MatchConstraints;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

@Entity
@Table(	name = "matches")
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ToString
@MatchConstraints(
        homeTeam = "homeTeam",
        awayTeam = "awayTeam",
        message = "HomeTeam can't be equal as awayTeam"
)
public class Match {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @OneToOne(cascade = {CascadeType.MERGE, CascadeType.PERSIST})
    private Team homeTeam;
    @OneToOne(cascade = {CascadeType.MERGE, CascadeType.PERSIST})
    private Team awayTeam;
    @OneToOne(cascade = {CascadeType.MERGE, CascadeType.PERSIST})
    private League league;
    private int homeTeamScore;
    private int awayTeamScore;
    private LocalDate date;
    private LocalTime time;
    @PrePersist
    private void validateTeams() {
        if (homeTeam != null && homeTeam.equals(awayTeam)) {
            throw new IllegalArgumentException("awayTeam and homeTeam cannot be the same.");
        }
    }
}
