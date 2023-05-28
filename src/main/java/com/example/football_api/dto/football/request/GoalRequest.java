package com.example.football_api.dto.football.request;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Builder
@ToString
@Getter
@Setter
public class GoalRequest {
    @NotNull
    private Long matchId;
    @NotNull
    private Long playerId;
    private boolean isOwn;
    @Min(0)
    @Max(150)
    private int time;
}
// TODO  goal validation if player couldn't play in match cuz of , table of history?? player-teams, unidirectional relations, so add to player OneToMany with clubs?? i guess so.
