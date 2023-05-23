package com.example.football_api.dto.football.request;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Builder
@ToString
@Getter
@Setter
public class GoalRequest {
    private Long id;
    private Long matchId;
    private Long playerId;
    private boolean isOwn;
}
