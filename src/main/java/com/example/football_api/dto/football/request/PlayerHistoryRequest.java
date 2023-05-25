package com.example.football_api.dto.football.request;

import com.example.football_api.entities.football.Player;
import com.example.football_api.entities.football.Team;
import jakarta.persistence.*;
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
public class PlayerHistoryRequest {
    private Long playerId;
    @NotNull
    private Long teamId;
    private LocalDate start;
    private LocalDate ends;
}
