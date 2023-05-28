package com.example.football_api.dto.football.response;

import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

@Builder
@ToString
@Getter
public class GoalShortResponse {
    private Long id;
    private Long matchId;
    private Long playerId;
    private boolean isOwn;
    private int time;
}
