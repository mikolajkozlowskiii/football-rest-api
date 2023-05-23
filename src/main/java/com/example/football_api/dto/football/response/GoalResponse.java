package com.example.football_api.dto.football.response;

import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

@Builder
@ToString
@Getter
public class GoalResponse {
    private Long id;
    private MatchResponse matchResponse;
    private PlayerResponse playerResponse;
    private boolean isOwn;
}
