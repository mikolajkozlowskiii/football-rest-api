package com.example.football_api.dto.football.response.overview;

import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

@Builder
@ToString
@Getter
public class GoalOverview {
    private Long goalId;
    private Long playerId;
    private String playerLastName;
    private int time;
    private boolean isOwn;
}
