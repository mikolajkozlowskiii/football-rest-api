package com.example.football_api.entities.football;


import com.example.football_api.exceptions.validators.ValidateMatchTeams;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(	name = "matches")
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ValidateMatchTeams(
        homeTeam = "homeTeam",
        awayTeam = "awayTeam",
        message = "HomeTeam can't be equal as awayTeam"
)
public class Match {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @OneToOne
    private Team homeTeam;
    @OneToOne
    private Team awayTeam;
    @OneToOne
    private League league;
    private int homeTeamScore;
    private int awayTeamScore;
    private LocalDateTime dateTime;
    @PrePersist
    private void validateTeams() {
        if (homeTeam != null && homeTeam.equals(awayTeam)) {
            throw new IllegalArgumentException("awayTeam and homeTeam cannot be the same.");
        }
    }
}
