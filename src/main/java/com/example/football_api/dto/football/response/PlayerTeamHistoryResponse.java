package com.example.football_api.dto.football.response;

import com.example.football_api.entities.football.Player;
import com.example.football_api.entities.football.Team;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

import java.time.LocalDate;

@Builder
@ToString
@Getter
public class PlayerTeamHistoryResponse {
    private Long id;
    private Long playerId;
    private Long teamId;
    private LocalDate start;
    private LocalDate ends;
}
